package ru.yandex.practicum.exceptions;

public class GameAlreadyFinishedException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public GameAlreadyFinishedException() {
        super("Игра уже завершена");
    }

    public GameAlreadyFinishedException(String message) {
        super(message);
    }

    public GameAlreadyFinishedException(String message, Throwable cause) {
        super(message, cause);
    }
}
