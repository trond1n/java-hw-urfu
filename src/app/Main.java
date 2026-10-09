package app;

import app.model.Student;
import app.validation.StudentValidator;
import app.validation.ValidationException;

public class Main {

    public static void main(String[] args) {
        try {
            Student s = StudentValidator.createValidated("", 7, "abc");
            System.out.println("Создан: " + s);
        } catch (ValidationException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }

        try {
            Student s = StudentValidator.createValidated("Иван Петров", 4.5, "123456");
            System.out.println("Создан: " + s);
        } catch (ValidationException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}