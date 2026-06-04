package ru.yandex.practicum;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ru.yandex.practicum.exceptions.InvalidWordLengthException;
import ru.yandex.practicum.exceptions.WordNotInDictionaryException;

public class WordleGame {

  private static final int MAX_STEPS = 6;
  private static final int WORD_LENGTH = 5;
  private final ArrayList<String> guesses;
  private final ArrayList<String> hints;
  private final WordleDictionary wordleDictionary;
  private final PrintWriter log;
  private String answer;
  private int steps;
  private boolean isGameOver;
  private boolean isVictory;
  private int lastHintCorrectCount;

  public WordleGame(WordleDictionary wordleDictionary, PrintWriter log) {

    this.wordleDictionary = wordleDictionary;
    this.log = log;
    this.steps = MAX_STEPS;
    this.guesses = new ArrayList<>();
    this.hints = new ArrayList<>();
    this.isGameOver = false;
    this.isVictory = false;
    this.lastHintCorrectCount = 0;

  }

  public void startGame() {
    this.answer = wordleDictionary.getRandomWord();
    log.println(
        "[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + " "
            + super.getClass().getSimpleName() + "-->"
            + Thread.currentThread().getStackTrace()[1].getMethodName() + "]"
            + " загадано слово: " + answer);
    log.flush();
  }

  public String makeGuess(String guess) {

    if (guess == null || guess.isEmpty()) {
      throw new InvalidWordLengthException("Слово не может быть пустым");
    }

    if (guess.length() != WORD_LENGTH) {
      throw new InvalidWordLengthException(guess);
    }

    if (!wordleDictionary.containsWord(guess)) {
      throw new WordNotInDictionaryException(guess);
    }

    if (isGameOver || isVictory) {
      return analyzeGuess(guess);
    }

    steps--;
    if (guess.equals(answer)) {
      isVictory = true;
    } else if (steps == 0) {
      isGameOver = true;
    }

    guesses.add(guess);

    return analyzeGuess(guess);
  }

  private String analyzeGuess(String guess) {
    int arrayLength = answer.length();
    char[] result = new char[arrayLength];
    char[] answerChars = answer.toCharArray();
    char[] guessChars = guess.toCharArray();
    boolean[] answerUsed = new boolean[arrayLength];

    for (int i = 0; i < arrayLength; i++) {
      if (guessChars[i] == answerChars[i]) {
        result[i] = '+';
        answerUsed[i] = true;
      } else {
        result[i] = '-';
      }
    }

    for (int i = 0; i < arrayLength; i++) {
      if (result[i] == '-') {
        for (int j = 0; j < arrayLength; j++) {
          if (!answerUsed[j] && guessChars[i] == answerChars[j]) {
            result[i] = '^';
            answerUsed[j] = true;
            break;
          }
        }
      }
    }

    return new String(result);
  }

  public String getHint() {

    if (guesses.isEmpty()) {
      String hint = wordleDictionary.getRandomWord();
      hints.add(hint);
      lastHintCorrectCount = 0;
      return hint;
    }

    Map<Character, Set<Integer>> correctPositions = new HashMap<>();
    Set<Character> containsLetters = new HashSet<>();
    Set<Character> excludedLetters = new HashSet<>();

    for (String guess : guesses) {
      String result = analyzeGuess(guess);

      for (int i = 0; i < WORD_LENGTH; i++) {
        char c = guess.charAt(i);
        char r = result.charAt(i);

        if (r == '+') {

          correctPositions.computeIfAbsent(c, k -> new HashSet<>()).add(i);
          containsLetters.add(c);
        } else if (r == '^') {

          containsLetters.add(c);
        } else if (r == '-') {

          boolean foundElsewhere = false;
          for (int j = 0; j < WORD_LENGTH; j++) {
            if (j != i && guess.charAt(j) == c) {
              char otherResult = result.charAt(j);
              if (otherResult == '+' || otherResult == '^') {
                foundElsewhere = true;
                break;
              }
            }
          }
          if (!foundElsewhere) {
            excludedLetters.add(c);
          }
        }
      }
    }

    List<String> filteredWords = wordleDictionary.getFilteredWords(
        correctPositions, containsLetters, excludedLetters);

    if (filteredWords == null || filteredWords.isEmpty()) {
      String hint = wordleDictionary.getRandomWord();
      hints.add(hint);
      return hint;
    }

    String hint = null;
    List<String> excludedHints = new ArrayList<>(hints);
    excludedHints.addAll(guesses);

    int requiredCorrectCount = lastHintCorrectCount + 1;

    for (String word : filteredWords) {
      if (!excludedHints.contains(word)) {
        String result = analyzeGuess(word);
        int correctCount = 0;
        for (int i = 0; i < result.length(); i++) {
          if (result.charAt(i) == '+') {
            correctCount++;
          }
        }
        if (correctCount >= requiredCorrectCount) {
          hint = word;
          lastHintCorrectCount = correctCount;
          break;
        }
      }
    }

    if (hint == null) {
      for (String word : filteredWords) {
        if (!excludedHints.contains(word)) {
          hint = word;
          String result = analyzeGuess(hint);
          int correctCount = 0;
          for (int i = 0; i < result.length(); i++) {
            if (result.charAt(i) == '+') {
              correctCount++;
            }
          }
          lastHintCorrectCount = correctCount;
          break;
        }
      }
    }

    if (hint == null) {
      hint = filteredWords.getFirst();
      String result = analyzeGuess(hint);
      int correctCount = 0;
      for (int i = 0; i < result.length(); i++) {
        if (result.charAt(i) == '+') {
          correctCount++;
        }
      }
      lastHintCorrectCount = correctCount;
    }

    hints.add(hint);
    return hint;
  }

  public String getAnswer() {
    return answer;
  }

  public boolean isGameOver() {
    return isGameOver;
  }

  public boolean isVictory() {
    return isVictory;
  }

  public int getSteps() {
    return steps;
  }
}
