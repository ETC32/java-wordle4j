package ru.yandex.practicum.exceptions;

public class InvalidWordLengthException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InvalidWordLengthException() {
        super("Слово должно содержать 5 букв");
    }

    public InvalidWordLengthException(String word) {
        super("Слово '" + word + "' имеет неправильную длину. Ожидается 5 букв.");
    }

    public InvalidWordLengthException(String message, Throwable cause) {
        super(message, cause);
    }
}
