package ru.yandex.practicum;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import ru.yandex.practicum.exceptions.GameAlreadyFinishedException;
import ru.yandex.practicum.exceptions.InvalidWordLengthException;
import ru.yandex.practicum.exceptions.WordNotInDictionaryException;

public class Wordle {

  private static final String DICT_FILENAME = "words_ru.txt";
  private static final String LOG_FILENAME = "wordle.log";
  private PrintWriter log;
  private WordleDictionary wordleDictionary;
  private WordleGame wordleGame;
  private Scanner sc;

  public static void main(String[] args) {

    Wordle wordle = new Wordle();
    wordle.run();

  }

  public void run() {

    try {
      log = initLog();
      log.println(
          "[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + " "
              + Wordle.class.getSimpleName() + "-->"
              + Thread.currentThread().getStackTrace()[1].getMethodName() + "]"
              + " Игра запущена");

      wordleDictionary = loadWordleDictionary();
      wordleGame = new WordleGame(wordleDictionary, log);
      wordleGame.startGame();

      sc = new Scanner(System.in);

      playGame();

      printResult();


    } catch (Exception e) {
      if (log != null) {
        log.println(
            "[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
                + " "
                + Wordle.class.getSimpleName() + "-->"
                + Thread.currentThread().getStackTrace()[1].getMethodName() + "]"
                + " Ошибка: " + e.getMessage());
        e.printStackTrace(log);
      }
    } finally {
      if (log != null) {
        log.close();
      }
      if (sc != null) {
        sc.close();
      }
    }
  }


  public void playGame() {
    System.out.println("Добро пожаловать в Wordle!");
    System.out.println("Угадайте слово из 5 букв за 6 попыток.");
    System.out.println(
        "Символы: + - буква на правильном месте, ^ - буква есть в слове, - - буквы нет.");
    System.out.println("Нажмите Enter для подсказки.");
    System.out.println();

    while (!wordleGame.isGameOver() && !wordleGame.isVictory()) {
      System.out.print("-->");
      String input = sc.nextLine().trim();
      input = wordleDictionary.normalizeWord(input);

      try {
        if (input.isEmpty()) {
          System.out.println("Подсказка: " + wordleGame.getHint());
        } else {
          System.out.println(">" + wordleGame.makeGuess(input));
        }
      } catch (InvalidWordLengthException e) {
        System.out.println("Неверный ввод! Введите слово из 5 букв.");
        if (log != null) {
          log.println(
              "[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
                  + " "
                  + Wordle.class.getSimpleName() + "-->"
                  + Thread.currentThread().getStackTrace()[1].getMethodName() + "]"
                  + " Ошибка: " + e.getMessage());
        }
      } catch (WordNotInDictionaryException e) {
        System.out.println("Слова нет в словаре! Повторите ввод.");
        if (log != null) {
          log.println(
              "[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
                  + " "
                  + Wordle.class.getSimpleName() + "-->"
                  + Thread.currentThread().getStackTrace()[1].getMethodName() + "]"
                  + " Ошибка: " + e.getMessage());
        }
      } catch (GameAlreadyFinishedException e) {
        break;
      }
    }
  }

  public void printResult() {
    System.out.println();
    if (wordleGame.isVictory()) {
      System.out.println("Вы угадали слово: " + wordleGame.getAnswer() + " Поздравляем!");
    } else {
      System.out.println("Игра окончена. Загаданное слово: " + wordleGame.getAnswer());
    }
  }

  public WordleDictionary loadWordleDictionary() {
    WordleDictionaryLoader wdl = new WordleDictionaryLoader();
    return wdl.loadDict(DICT_FILENAME, log);
  }

  private PrintWriter initLog() throws IOException {
    return new PrintWriter(LOG_FILENAME, StandardCharsets.UTF_8);

  }
}





