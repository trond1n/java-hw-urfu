package app.model;

public class Student {
    private final String name;
    private final double averageGrade;
    private final String recordBookNumber;


    public Student(String name, double averageGrade, String recordBookNumber) {
        this.name = name;
        this.averageGrade = averageGrade;
        this.recordBookNumber = recordBookNumber;
    }

    public String getName() {
        return name;
    }

    public double getAverageGrade() {
        return averageGrade;
    }

    public String getRecordBookNumber() {
        return recordBookNumber;
    }

    @Override
    public String toString() {
        return String.format("%-20s | балл: %.2f | зачетка: %s",
                name, averageGrade, recordBookNumber);
    }
}

