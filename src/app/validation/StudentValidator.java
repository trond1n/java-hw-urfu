package app.validation;

import app.model.Student;

public class StudentValidator {

    private static final String NAME_REGEX =
            "^[A-ZА-ЯЁ][a-zа-яё]+(?:[- ][A-ZА-ЯЁ][a-zа-яё]+)*$";

    public static void validateName(String name) throws ValidationException {
        if (name == null || name.length() < 2 || name.length() > 50) {
            throw new ValidationException(
                    "Имя должно содержать от 2 до 50 символов, получено: \"" + name + "\"."
            );
        }

        if (!name.matches(NAME_REGEX)) {
            throw new ValidationException(
                    "Имя должно состоять из слов с заглавной буквы, получено: \"" + name + "\"."
            );
        }
    }


    public static void validateGrade(double grade) throws ValidationException {
        if (grade < 0.0 || grade > 5.0) {
            throw new ValidationException("Оценка должна быть от 0 до 5, получено: " + String.format("%.2f", grade));
        }
    }

    public static void validateRecordBookNumber(String number) throws ValidationException {
        if (number == null || !number.matches("\\d{6}")) {
            throw new ValidationException("Номер зачетки - ровно 6 цифр, получено: \"" + number + "\"");
        }
    }

    public static Student createValidated(String name, double grade, String number) throws ValidationException {
        validateName(name);
        validateGrade(grade);
        validateRecordBookNumber(number);
        return new Student(name, grade, number);
    }
}
