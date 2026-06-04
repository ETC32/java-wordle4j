package ru.yandex.practicum.exceptions;

public class EmptyDictionaryException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public EmptyDictionaryException() {
        super("Словарь пуст после загрузки");
    }

    public EmptyDictionaryException(String message) {
        super(message);
    }

    public EmptyDictionaryException(String message, Throwable cause) {
        super(message, cause);
    }
}
