package ru.yandex.practicum;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.InvalidWordLengthException;
import ru.yandex.practicum.exceptions.WordNotInDictionaryException;

class WordleGameTest {

  private WordleGame game;
  private WordleDictionary dictionary;

  @BeforeEach
  void setUp() {
    WordleDictionaryLoader loader = new WordleDictionaryLoader();
    StringWriter stringWriter = new StringWriter();
    PrintWriter log = new PrintWriter(stringWriter);
    dictionary = loader.loadDict("test_words.txt", log);
    game = new WordleGame(dictionary, log);
  }

  @Test
  void testStartGame() {
    game.startGame();
    String answer = game.getAnswer();
    assertNotNull(answer);
    assertEquals(5, answer.length());
    assertTrue(dictionary.containsWord(answer));
  }

  @Test
  void testMakeGuess_CorrectAnswer() {
    game.startGame();
    String answer = game.getAnswer();

    String result = game.makeGuess(answer);

    assertEquals("+++++", result);
    assertTrue(game.isVictory());
    assertFalse(game.isGameOver());
  }

  @Test
  void testMakeGuess_WrongAnswer() {
    game.startGame();
    String answer = game.getAnswer();

    String wrongGuess = answer.equals("домой") ? "дачка" : "домой";
    String result = game.makeGuess(wrongGuess);

    assertNotEquals("+++++", result);
    assertFalse(game.isVictory());
  }

  @Test
  void testMakeGuess_DecrementsSteps() {
    game.startGame();
    int initialSteps = game.getSteps();

    game.makeGuess("дачка");

    assertEquals(initialSteps - 1, game.getSteps());
  }

  @Test
  void testMakeGuess_AddsToGuesses() {
    game.startGame();
    assertEquals(0, getGuessesCount(game));

    game.makeGuess("дачка");

    assertEquals(1, getGuessesCount(game));
  }

  @Test
  void testMakeGuess_GameOverAfterSixAttempts() {
    game.startGame();

    for (int i = 0; i < 6; i++) {
      game.makeGuess("столы");
    }

    assertTrue(game.isGameOver());
    assertFalse(game.isVictory());
  }

  @Test
  void testMakeGuess_VictoryStopsGame() {
    game.startGame();
    String answer = game.getAnswer();

    for (int i = 0; i < 6; i++) {
      game.makeGuess(i == 0 ? answer : "столы");
    }

    assertTrue(game.isVictory());
    assertFalse(game.isGameOver());
  }

  @Test
  void testMakeGuess_ExactMatch() {
    game.startGame();
    setAnswer(game, "домой");

    String result = game.makeGuess("домой");
    assertEquals("+++++", result);
  }

  @Test
  void testMakeGuess_NoMatch() {
    game.startGame();
    setAnswer(game, "домой");

    String result = game.makeGuess("варан");
    assertEquals("-----", result);
  }

  @Test
  void testMakeGuess_PartialMatch() {
    game.startGame();
    setAnswer(game, "оплуг");

    String result = game.makeGuess("округ");
    assertEquals("+--++", result);
  }

  @Test
  void testMakeGuess_DuplicateLetters() {
    game.startGame();
    setAnswer(game, "кошка");

    String result = game.makeGuess("кошка");
    assertEquals("+++++", result);
  }

  @Test
  void testMakeGuess_DuplicateLettersPartial() {
    game.startGame();
    setAnswer(game, "оазис");

    String result = game.makeGuess("округ");
    assertEquals("+----", result);
  }

  @Test
  void testGetHint_NoGuesses() {
    game.startGame();

    String hint = game.getHint();

    assertNotNull(hint);
    assertEquals(5, hint.length());
    assertTrue(dictionary.containsWord(hint));
  }

  @Test
  void testGetHint_AddsToHints() {
    game.startGame();
    assertEquals(0, getHintsCount(game));

    game.getHint();

    assertEquals(1, getHintsCount(game));
  }

  @Test
  void testGetHint_WithGuesses() {
    game.startGame();
    setAnswer(game, "домой");

    game.makeGuess("дачка");

    String hint = game.getHint();

    assertNotNull(hint);
    assertTrue(hint.contains("д") || dictionary.containsWord(hint));
  }

  @Test
  void testGetHint_DoesNotAffectSteps() {
    game.startGame();
    int initialSteps = game.getSteps();

    game.getHint();

    assertEquals(initialSteps, game.getSteps());
  }

  @Test
  void testGetHint_DoesNotAddToGuesses() {
    game.startGame();
    assertEquals(0, getGuessesCount(game));

    game.getHint();

    assertEquals(0, getGuessesCount(game));
  }

  @Test
  void testGetHint_ExcludesUsedHints() {
    game.startGame();

    String hint1 = game.getHint();
    String hint2 = game.getHint();

    assertNotEquals(hint1, hint2);
  }

  @Test
  void testGetSteps() {
    game.startGame();
    assertEquals(6, game.getSteps());

    game.makeGuess("домой");
    assertEquals(5, game.getSteps());
  }

  @Test
  void testGetAnswer() {
    game.startGame();
    String answer = game.getAnswer();
    assertNotNull(answer);
    assertEquals(5, answer.length());
  }

  @Test
  void testIsGameOver() {
    game.startGame();
    assertFalse(game.isGameOver());

    for (int i = 0; i < 6; i++) {
      game.makeGuess("столы");
    }

    assertTrue(game.isGameOver());
  }

  @Test
  void testIsVictory() {
    game.startGame();
    assertFalse(game.isVictory());

    setAnswer(game, "домой");
    game.makeGuess("домой");

    assertTrue(game.isVictory());
  }

  @Test
  void testMakeGuess_WordTooLong_ThrowsException() {
    game.startGame();

    assertThrows(InvalidWordLengthException.class, () -> {
      game.makeGuess("домовой");
    });
  }

  @Test
  void testMakeGuess_WordTooShort_ThrowsException() {
    game.startGame();

    assertThrows(InvalidWordLengthException.class, () -> {
      game.makeGuess("дом");
    });
  }

  @Test
  void testMakeGuess_WordNotInDictionary_ThrowsException() {
    game.startGame();

    assertThrows(WordNotInDictionaryException.class, () -> {
      game.makeGuess("абвгд");
    });
  }

  @Test
  void testMakeGuess_EmptyWord_ThrowsException() {
    game.startGame();

    assertThrows(InvalidWordLengthException.class, () -> {
      game.makeGuess("");
    });
  }

  private int getGuessesCount(WordleGame game) {
    try {
      java.lang.reflect.Field field = WordleGame.class.getDeclaredField("guesses");
      field.setAccessible(true);
      ArrayList<?> guesses = (ArrayList<?>) field.get(game);
      return guesses.size();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private int getHintsCount(WordleGame game) {
    try {
      java.lang.reflect.Field field = WordleGame.class.getDeclaredField("hints");
      field.setAccessible(true);
      ArrayList<?> hints = (ArrayList<?>) field.get(game);
      return hints.size();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void setAnswer(WordleGame game, String answer) {
    try {
      java.lang.reflect.Field field = WordleGame.class.getDeclaredField("answer");
      field.setAccessible(true);
      field.set(game, answer);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
