package app.service;

import app.model.Student;

import java.util.Comparator;
import java.util.List;

public class StudentService {
    public List<Student> filterByMinGrade(List<Student> students, double minGrade) {
        return students.stream()
                .filter(s -> s.getAverageGrade() >= minGrade)
                .toList();
    }

    public void printReport(List<Student> students) {
        students.stream()
                .sorted(Comparator.comparingDouble(Student::getAverageGrade).reversed())
                .map(s -> String.format("%s — %.2f", s.getName().toUpperCase(), s.getAverageGrade()))
                .forEach(System.out::println);
    }
}
