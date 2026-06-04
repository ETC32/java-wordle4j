package ru.yandex.practicum;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.DictionaryLoadException;
import ru.yandex.practicum.exceptions.EmptyDictionaryException;
import ru.yandex.practicum.exceptions.GameAlreadyFinishedException;
import ru.yandex.practicum.exceptions.InvalidWordLengthException;
import ru.yandex.practicum.exceptions.WordNotInDictionaryException;

class ExceptionsTest {

  @Test
  void testEmptyDictionaryException() {
    EmptyDictionaryException exception = new EmptyDictionaryException("Test message");
    assertEquals("Test message", exception.getMessage());
  }

  @Test
  void testDictionaryLoadException() {
    DictionaryLoadException exception = new DictionaryLoadException("Test message");
    assertEquals("Test message", exception.getMessage());
  }

  @Test
  void testGameAlreadyFinishedException() {
    GameAlreadyFinishedException exception = new GameAlreadyFinishedException("Test message");
    assertEquals("Test message", exception.getMessage());
  }

  @Test
  void testInvalidWordLengthException() {
    InvalidWordLengthException exception = new InvalidWordLengthException("Test message");
    assertTrue(exception.getMessage().contains("Test message"));
  }

  @Test
  void testWordNotInDictionaryException() {
    WordNotInDictionaryException exception = new WordNotInDictionaryException("Test message");
    assertTrue(exception.getMessage().contains("Test message"));
  }

  @Test
  void testEmptyDictionaryException_DefaultMessage() {
    EmptyDictionaryException exception = new EmptyDictionaryException();
    assertNotNull(exception.getMessage());
  }

  @Test
  void testDictionaryLoadException_DefaultMessage() {
    DictionaryLoadException exception = new DictionaryLoadException();
    assertNotNull(exception.getMessage());
  }

  @Test
  void testGameAlreadyFinishedException_DefaultMessage() {
    GameAlreadyFinishedException exception = new GameAlreadyFinishedException();
    assertNotNull(exception.getMessage());
  }

  @Test
  void testInvalidWordLengthException_DefaultMessage() {
    InvalidWordLengthException exception = new InvalidWordLengthException();
    assertNotNull(exception.getMessage());
  }

  @Test
  void testWordNotInDictionaryException_DefaultMessage() {
    WordNotInDictionaryException exception = new WordNotInDictionaryException();
    assertNotNull(exception.getMessage());
  }
}
