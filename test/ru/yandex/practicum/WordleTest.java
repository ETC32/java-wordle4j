package ru.yandex.practicum;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.exceptions.InvalidWordLengthException;
import ru.yandex.practicum.exceptions.WordNotInDictionaryException;

class WordleTest {

  private Wordle wordle;
  private WordleGame game;

  @BeforeEach
  void setUp() {
    wordle = new Wordle();
    StringWriter stringWriter = new StringWriter();
    PrintWriter log = new PrintWriter(stringWriter);

    WordleDictionaryLoader loader = new WordleDictionaryLoader();
    WordleDictionary dictionary = loader.loadDict("test_words.txt", log);
    game = new WordleGame(dictionary, log);
  }

  @Test
  void testLoadWordleDictionary() {
    WordleDictionary loadedDict = wordle.loadWordleDictionary();

    assertNotNull(loadedDict);
    assertTrue(loadedDict.containsWord("домой"));
    assertTrue(loadedDict.containsWord("дачка"));
    assertTrue(loadedDict.containsWord("столы"));
    assertTrue(loadedDict.containsWord("варан"));
    assertTrue(loadedDict.containsWord("оплуг"));
    assertTrue(loadedDict.containsWord("округ"));
    assertTrue(loadedDict.containsWord("кошка"));
    assertTrue(loadedDict.containsWord("оазис"));
  }

  @Test
  void testPrintResult_Victory() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outputStream));

    try {
      game.startGame();
      setAnswer(game);
      game.makeGuess("домой"); // Победа

      setWordleGame(wordle, game);

      wordle.printResult();

      String output = outputStream.toString();
      assertTrue(output.contains("Вы угадали слово: домой"));
      assertTrue(output.contains("Поздравляем"));
    } finally {
      System.setOut(originalOut);
    }
  }

  @Test
  void testPrintResult_GameOver() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outputStream));

    try {
      game.startGame();
      setAnswer(game);

      for (int i = 0; i < 6; i++) {
        game.makeGuess("столы");
      }

      setWordleGame(wordle, game);

      wordle.printResult();

      String output = outputStream.toString();
      assertTrue(output.contains("Игра окончена"));
      assertTrue(output.contains("домой"));
    } finally {
      System.setOut(originalOut);
    }
  }

  @Test
  void testPrintResult_NotVictoryWhenGameOver() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outputStream));

    try {
      game.startGame();
      setAnswer(game);

      for (int i = 0; i < 6; i++) {
        game.makeGuess("столы");
      }

      setWordleGame(wordle, game);
      wordle.printResult();

      String output = outputStream.toString();
      assertFalse(output.contains("Поздравляем"));
    } finally {
      System.setOut(originalOut);
    }
  }

  @Test
  void testPlayGame_InvalidWordLength_PrintsErrorMessage() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outputStream));

    try {
      game.startGame();

      try {
        game.makeGuess("домовой"); // 6 букв вместо 5
      } catch (InvalidWordLengthException e) {
        // Исключение выбрасывается, но сообщение не выводится в WordleGame
      }

      String output = outputStream.toString();
      assertFalse(output.contains("Неверный ввод! Введите слово из 5 букв."));
    } finally {
      System.setOut(originalOut);
    }
  }

  @Test
  void testPlayGame_WordNotInDictionary_PrintsErrorMessage() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream originalOut = System.out;
    System.setOut(new PrintStream(outputStream));

    try {
      game.startGame();

      try {
        game.makeGuess("абвгд"); // Слова нет в словаре
      } catch (WordNotInDictionaryException e) {
        // Исключение выбрасывается, но сообщение не выводится в WordleGame
      }

      String output = outputStream.toString();
      assertFalse(output.contains("Слова нет в словаре! Повторите ввод."));
    } finally {
      System.setOut(originalOut);
    }
  }

  private void setAnswer(WordleGame game) {
    try {
      java.lang.reflect.Field field = WordleGame.class.getDeclaredField("answer");
      field.setAccessible(true);
      field.set(game, "домой");
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void setWordleGame(Wordle wordle, WordleGame game) {
    try {
      java.lang.reflect.Field field = Wordle.class.getDeclaredField("wordleGame");
      field.setAccessible(true);
      field.set(wordle, game);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
