package app.io;

import java.util.Scanner;

public class ConsoleInput {

    private final Scanner scanner;

    public ConsoleInput(Scanner scanner) {
        this.scanner = scanner;
    }

    public String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public int readInt(String prompt) {
        while (true) {
            String input = readLine(prompt);
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: нужно целое число.");
            }
        }
    }
    public int readDouble(String prompt)  {
        while (true) {
            String input = readLine(prompt);
            try {
                return (int) Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: некорректное значение.");
            }
        }
    }
}