package ru.yandex.practicum.exceptions;

import java.io.Serial;

public class WordNotInDictionaryException extends RuntimeException {

  @Serial
  private static final long serialVersionUID = -3544844593036130768L;

  public WordNotInDictionaryException() {
        super("Слово отсутствует в словаре");
    }

    public WordNotInDictionaryException(String word) {
        super("Слово '" + word + "' отсутствует в словаре");
    }

    public WordNotInDictionaryException(String message, Throwable cause) {
        super(message, cause);
    }
}
