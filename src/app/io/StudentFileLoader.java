package app.io;

import app.model.Student;
import app.validation.StudentValidator;
import app.validation.ValidationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class StudentFileLoader {
    public List<Student> load(String filePath) throws IOException {
        List<Student> result = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Path.of(filePath), StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (lineNumber == 1) {
                    continue;
                }
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split(";", -1);
                if (parts.length != 3) {
                    System.out.println("ожидалось 3 поля, найдено " + parts.length);
                    continue;
                }
                try {
                    String name = parts[0].trim();
                    double grade = Double.parseDouble(parts[1].trim().replace(',', '.'));
                    String number = parts[2].trim();

                    result.add(StudentValidator.createValidated(name, grade, number));
                } catch (NumberFormatException | ValidationException e) {
                    System.out.println("Строка " + lineNumber + " пропущена: " + e.getMessage());
                }

            }
        }
        return result;
    }
}

