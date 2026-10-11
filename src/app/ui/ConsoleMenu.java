package app.ui;

import app.io.ConsoleInput;
import app.io.InputClosedException;
import app.io.StudentFileLoader;
import app.model.Student;
import app.repository.StudentRepository;
import app.service.StudentService;
import app.sort.BubbleSort;
import app.sort.InsertionSort;
import app.sort.SortAlgorithm;
import app.validation.StudentValidator;
import app.validation.ValidationException;

import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.util.Comparator;
import java.util.List;

public class ConsoleMenu {
    private static final String DEFAULT_FILE_PATH = "data/students.csv";

    private final ConsoleInput consoleInput;
    private final StudentRepository studentRepository;
    private final StudentFileLoader studentFileLoader;
    private final StudentService studentService;
    private final List<SortAlgorithm<Student>> algorithms = List.of(
            new BubbleSort<>(),
            new InsertionSort<>()
    );

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
        try {
            while (true) {
                printMenu();

                int command = consoleInput.readInt("Выберите пункт меню: ");

                switch (command) {
                    case 1 -> addManually();
                    case 2 -> loadFromFile();
                    case 3 -> processData();
                    case 4 -> showAll();
                    case 5 -> removeStudent();
                    case 6 -> sortWithCustomAlgorithm();
                    case 7 -> showStudentByNumber();
                    case 0 -> {
                        System.out.println("Завершение работы программы...");
                        return;
                    }
                    default -> System.out.println("Неизвестная команда.");
                }
            }
        } catch (InputClosedException e) {
            System.out.println();
            System.out.println("Ввод завершён. Завершение работы программы...");
        }
    }

    private void sortWithCustomAlgorithm() {
        List<Student> students = studentRepository.getAll();

        if (students.isEmpty()) {
            System.out.println("Список пуст.");
            return;
        }

        System.out.println("Доступные алгоритмы:");

        for (int i = 0; i < algorithms.size(); i++) {
            System.out.println((i + 1) + ". " + algorithms.get(i).getName());
        }

        int algorithmNumber = consoleInput.readInt("Выберите сортировку: ");

        if (algorithmNumber < 1 || algorithmNumber > algorithms.size()) {
            System.out.println("Нет алгоритма с номером " + algorithmNumber + ".");
            return;
        }

        System.out.println("""
                Поле сортировки:
                1. По баллу
                2. По имени
                """);

        int fieldChoice = consoleInput.readInt("Выберите поле: ");

        Comparator<Student> comparator = switch (fieldChoice) {
            case 1 -> Comparator.comparingDouble(Student::getAverageGrade);
            case 2 -> Comparator.comparing(Student::getName);
            default -> null;
        };

        if (comparator == null) {
            System.out.println("Неизвестное поле сортировки.");
            return;
        }

        System.out.println("""
                Направление сортировки:
                1. По возрастанию
                2. По убыванию
                """);

        int direction = consoleInput.readInt("Выберите направление: ");

        if (direction == 2) {
            comparator = comparator.reversed();
        } else if (direction != 1) {
            System.out.println("Неизвестное направление сортировки.");
            return;
        }

        SortAlgorithm<Student> algorithm = algorithms.get(algorithmNumber - 1);
        List<Student> sortedStudents = algorithm.sort(students, comparator);

        System.out.println("Результат сортировки:");

        for (Student student : sortedStudents) {
            System.out.println(student);
        }
    }

    private void printMenu() {
        System.out.println("""
                
                ===== МЕНЮ =====
                1. Добавить данные вручную
                2. Загрузить данные из файла
                3. Обработать данные
                4. Показать всех студентов
                5. Удалить студента
                6. Выбрать алгоритм сортировки
                7. Показать студента по номеру
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

    private void removeStudent() {
        List<Student> students = studentRepository.getAll();

        if (students.isEmpty()) {
            System.out.println("Список пуст.");
            return;
        }

        System.out.println("Список студентов:");

        for (int i = 0; i < students.size(); i++) {
            System.out.println((i + 1) + ". " + students.get(i));
        }

        int number = consoleInput.readInt("Введите номер студента для удаления: ");
        try {
            Student removed = studentRepository.remove(number - 1);
            System.out.println("Удалён студент: " + removed);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Нет студента с номером " + number + ".");
        }
    }

    private void showStudentByNumber() {
        List<Student> students = studentRepository.getAll();

        if (students.isEmpty()) {
            System.out.println("Список пуст.");
            return;
        }

        System.out.println("Список студентов:");

        for (int i = 0; i < students.size(); i++) {
            System.out.println((i + 1) + ". " + students.get(i));
        }

        int number = consoleInput.readInt("Введите номер студента: ");

        try {
            System.out.println(studentRepository.get(number - 1));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Нет студента с номером " + number + ".");
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
        } catch (InvalidPathException e) {
            System.out.println("Некорректный путь к файлу: " + path);
        }
    }

    private void showAll() {
        if (studentRepository.isEmpty()) {
            System.out.println("Список пуст.");
            return;
        }

        System.out.println("Список студентов (" + studentRepository.size() + "):");
        System.out.println(studentRepository);
    }

    private void processData() {
        List<Student> students = studentRepository.getAll();

        if (students.isEmpty()) {
            System.out.println("Нет данных для обработки.");
            return;
        }
        double threshold = consoleInput.readDouble("Введите порог балла: ");

        List<Student> passed = studentService.filterByMinGrade(students, threshold);
        if (passed.isEmpty()) {
            System.out.println("Никто не прошел порог " + threshold + ".");
            return;
        }
        studentService.printReport(passed);
    }
}
