package app.ui;

import app.io.ConsoleInput;
import app.io.StudentFileLoader;
import app.model.Student;
import app.repository.StudentRepository;
import app.service.StudentService;
import app.validation.StudentValidator;
import app.validation.ValidationException;


import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;

public class ConsoleMenu {

    private static final String DEFAULT_FILE_PATH = "data/students.csv";

    private final ConsoleInput consoleInput;
    private final StudentRepository studentRepository;
    private final StudentFileLoader studentFileLoader;
    private final StudentService studentService;

    public ConsoleMenu(
            ConsoleInput consoleInput,
            StudentRepository studentRepository,
            StudentFileLoader studentFileLoader,
            StudentService studentService
    ) {
        this.consoleInput = consoleInput;
        this.studentRepository = studentRepository;
        this.studentFileLoader = studentFileLoader;
        this.studentService = studentService;
    }


    public void run() {
        System.out.println("Консольное меню запущено...");
        while (true) {
            printMenu();

            int command = consoleInput.readInt("Выберите пункт меню: ");

            switch (command) {
                case 1 -> addManually();
                case 2 -> loadFromFile();
                case 3 -> processData();
                case 4 -> showAll();
                case 0 -> {
                    System.out.println("Завершение работы программы...");
                    return;
                }
                default -> System.out.println("Неизвестная команда.");
            }
        }
    }

    private void printMenu() {
        System.out.println("""
                
                ===== МЕНЮ =====
                1. Добавить данные вручную
                2. Загрузить данные из файла
                3. Обработать данные
                4. Показать всех студентов
                0. Выход
                """);
    }

    private void addManually() {
        System.out.println("Добавление студента");

        String name = consoleInput.readLine("Введите имя: ");
        try {
            StudentValidator.validateName(name);
        } catch (ValidationException e) {
            System.out.println("Ошибка валидации: " + e.getMessage());
            return;
        }

        double grade = consoleInput.readDouble("Введите балл: ");
        try {
            StudentValidator.validateGrade(grade);
        } catch (ValidationException e) {
            System.out.println("Ошибка валидации: " + e.getMessage());
            return;
        }

        String recordBook = consoleInput.readLine("Введите номер зачетки: ");

        try {
            Student student = StudentValidator.createValidated(
                    name,
                    grade,
                    recordBook
            );

            studentRepository.addStudent(student);
            System.out.println("Студент успешно добавлен.");

        } catch (ValidationException e) {
            System.out.println("Ошибка валидации: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        String path = consoleInput.readLine("Введите путь к файлу (Enter — "
                + DEFAULT_FILE_PATH + "): ");

        if (path.isEmpty()) {
            path = DEFAULT_FILE_PATH;
        }

        try {
            List<Student> students = studentFileLoader.load(path);

            studentRepository.addAll(students);

            System.out.println("Загружено студентов: " + students.size());

        } catch (NoSuchFileException e) {
            System.out.println("Файл не найден: " + path);

        } catch (IOException e) {
            System.out.println("Ошибка чтения: " + e.getMessage());
        }
    }

    private void showAll() {
        List<Student> students = studentRepository.getAll();

        if (students.isEmpty()) {
            System.out.println("Список пуст.");
            return;
        }

        System.out.println("Список студентов:");

        for (Student student : students) {
            System.out.println(student);
        }
    }

    private void processData() {
        List<Student> students = studentRepository.getAll();

        if (students.isEmpty()) {
            System.out.println("Нет данных для обработки.");
            return;
        }
        double threshold = consoleInput.readDouble("Введите порог балла: ");

        if (studentService.filterByMinGrade(students, threshold).isEmpty()) {
            System.out.println("Никто не прошел порог " + threshold + ".");
            return;
        }
        studentService.printReport(students, threshold);
    }
}
