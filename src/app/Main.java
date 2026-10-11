package app;

import app.io.StudentFileLoader;
import app.model.Student;
import app.repository.StudentRepository;
import app.service.StudentService;
import app.validation.StudentValidator;
import app.validation.ValidationException;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Шаги 2–4: модель и валидация ===");
        checkCreate("", 4.5, "123456");
        checkCreate("Иван", 7, "123456");
        checkCreate("Иван", 4.5, "abc");
        checkCreate("Иван Петров", 4.5, "123456");

        System.out.println("\n=== Шаг 5: репозиторий ===");
        StudentRepository repository = new StudentRepository();
        System.out.println("Пустой в начале: " + repository.isEmpty());
        try {
            repository.addStudent(StudentValidator.createValidated("Анна Иванова", 4.2, "200001"));
        } catch (ValidationException e) {
            System.out.println("Неожиданная ошибка: " + e.getMessage());
        }
        System.out.println("Размер после add: " + repository.size());
        try {
            repository.getAll().clear();
        } catch (UnsupportedOperationException e) {
            System.out.println("getAll() вернул неизменяемую копию — ок");
        }
        System.out.println("Размер после попытки clear: " + repository.size());

        System.out.println("\n=== Шаги 7–8: загрузка из файла ===");
        StudentFileLoader loader = new StudentFileLoader();
        try {
            List<Student> loaded = loader.load("data/students.csv");
            repository.addAll(loaded);
            System.out.println("Загружено: " + loaded.size());
            System.out.println("Всего в репозитории: " + repository.size());
        } catch (IOException e) {
            System.out.println("Ошибка чтения: " + e.getMessage());
        }
        try {
            loader.load("data/nope.csv");
        } catch (IOException e) {
            System.out.println("Ожидаемая ошибка: " + e.getClass().getSimpleName()
                    + " — " + e.getMessage());
        }

        System.out.println("\n=== Шаг 9: Stream API ===");
        StudentService service = new StudentService();
        List<Student> all = repository.getAll();

        System.out.println("\n-- 9.1 filter: балл >= 4.0 --");
        service.filterByMinGrade(all, 4.0).forEach(System.out::println);

        System.out.println("\n-- 9.1 filter: балл >= 5.0 (ждём пустой результат) --");
        List<Student> nobody = service.filterByMinGrade(all, 5.0);
        System.out.println("Найдено: " + nobody.size());

        System.out.println("\n-- 9.2 map: имена --");
        service.mapToNames(all).forEach(System.out::println);

        System.out.println("\n-- 9.3 sorted: по баллу по возрастанию --");
        service.sortByGrade(all).forEach(System.out::println);

        System.out.println("\n-- Исходный список не изменился? --");
        System.out.println("Первый в репозитории: " + repository.getAll().get(0).getName());
        System.out.println("Размер: " + repository.size());

        System.out.println("\n-- 9.4 printReport: балл >= 4.0, по убыванию --");
        service.printReport(all, 4.0);

        System.out.println("\n-- printReport на пустом списке (не должен упасть) --");
        service.printReport(List.of(), 4.0);
        System.out.println("ок, не упал");
    }

    private static void checkCreate(String name, double grade, String number) {
        try {
            Student s = StudentValidator.createValidated(name, grade, number);
            System.out.println("Создан: " + s);
        } catch (ValidationException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}