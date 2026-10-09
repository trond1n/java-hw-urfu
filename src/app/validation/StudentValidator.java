package app.validation;

import app.model.Student;

public class StudentValidator {
    public static void validateName(String name) throws ValidationException {
        String regex = "^[A-ZА-ЯЁ][a-zа-яё]+(?:[- ][A-ZА-ЯЁ][a-zа-яё]+)*$";
        if (name == null || name.trim().isEmpty() || !name.matches(regex)) {
            throw new ValidationException("Ошибка в имени студента");
        }
    }

    public static void validateGrade(double grade) throws ValidationException {
        if (grade < 0.0 || grade > 5.0) {
            throw new ValidationException("Некорректная оценка");
        }
    }

    public static void validateRecordBookNumber(String number) throws ValidationException {
        if (number.length() != 6) {
            throw new ValidationException("Некорректный номер зачетки");
        }
    }

    public static Student createValidated(String name, double grade, String number)
            throws ValidationException {
        // TODO: вызвать три проверки, затем вернуть new Student(...)
        validateName(name);
        validateGrade(grade);
        validateRecordBookNumber(number);
        return new Student(name, grade, number);
    }
}
