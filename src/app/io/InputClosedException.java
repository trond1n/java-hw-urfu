package app.io;

public class InputClosedException extends RuntimeException {
    public InputClosedException() {
        super("Поток ввода закрыт");
    }
}
