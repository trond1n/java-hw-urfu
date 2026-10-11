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

    public List<String> mapToNames(List<Student> students) {
        return students.stream()
                .map(Student::getName)
                .map(String::toUpperCase)
                .toList();
    }

    public List<Student> sortByGrade(List<Student> students) {
        return students.stream()
                .sorted(Comparator.comparingDouble(Student::getAverageGrade))
                .toList();
    }

    public void printReport(List<Student> students, double minGrade) {
        students.stream()
                .filter(s -> s.getAverageGrade() >= minGrade)
                .sorted(Comparator.comparingDouble(Student::getAverageGrade).reversed())
                .map(s -> s.getName().toUpperCase())
                .forEach(System.out::println);
    }
}
