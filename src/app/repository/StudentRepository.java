package app.repository;

import app.model.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentRepository {
    private final List<Student> students = new ArrayList<>();

    public void addStudent(Student student) {
        students.add(student);
    }

    public void addAll(List<Student> list) {
        for (Student student : list) {
            addStudent(student);
        }
    }

    public List<Student> getAll() {
        return List.copyOf(students);
    }

    public int size() {
        return students.size();
    }

    public boolean isEmpty() {
        return students.isEmpty();
    }
}
