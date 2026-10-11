package app.repository;

import app.collection.MyArrayList;
import app.model.Student;

import java.util.List;

public class StudentRepository {
    private final MyArrayList<Student> students = new MyArrayList<>();

    public void addStudent(Student student) {
        students.add(student);
    }

    public void addAll(List<Student> list) {
        for (Student student : list) {
            addStudent(student);
        }
    }

    public List<Student> getAll() {
        return students.stream().toList();
    }

    public int size() {
        return students.size();
    }

    public boolean isEmpty() {
        return students.size() == 0;
    }

    public Student remove(int index) {
        return students.remove(index);
    }

    public Student get(int index) {
        return students.get(index);
    }

    @Override
    public String toString() {
        return students.toString();
    }
}
