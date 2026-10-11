package app;

import app.io.ConsoleInput;
import app.io.StudentFileLoader;
import app.model.Student;
import app.repository.StudentRepository;
import app.validation.StudentValidator;
import app.validation.ValidationException;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Шаги 2–4: модель и валидация ===");
        checkCreate("", 4.5, "123456");             // пустое имя
        checkCreate("Иван", 7, "123456");           // балл вне 0..5
        checkCreate("Иван", 4.5, "abc");            // зачётка не 6 цифр
        checkCreate("Иван Петров", 4.5, "123456");  // всё верно

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
            repository.getAll().clear();   // пытаемся испортить список снаружи
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
            loaded.forEach(System.out::println);
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

        System.out.println("\n=== Шаг 6: ввод с консоли ===");
        try (Scanner scanner = new Scanner(System.in)) {
            ConsoleInput input = new ConsoleInput(scanner);
            int number = input.readInt("Целое число (сначала введи abc): ");
            double grade = input.readDouble("Балл (введи 4,5 с запятой): ");
            String name = input.readLine("Имя (с пробелами по краям): ");
            System.out.println("Получено: " + number + " | " + grade + " | [" + name + "]");
        }
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