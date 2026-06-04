package ru.yandex.practicum.exceptions;

public class DictionaryLoadException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DictionaryLoadException() {
        super("Ошибка загрузки словаря");
    }

    public DictionaryLoadException(String message) {
        super(message);
    }

    public DictionaryLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
