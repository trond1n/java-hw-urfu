package app;

import app.io.ConsoleInput;
import app.io.StudentFileLoader;
import app.repository.StudentRepository;
import app.service.StudentService;
import app.ui.ConsoleMenu;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            ConsoleInput input = new ConsoleInput(scanner);
            StudentRepository repository = new StudentRepository();
            StudentFileLoader loader = new StudentFileLoader();
            StudentService service = new StudentService();

            new ConsoleMenu(input, repository, loader, service).run();
        }
    }
}