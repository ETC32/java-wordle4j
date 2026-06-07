package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import ru.yandex.practicum.exceptions.DictionaryLoadException;

public class WordleDictionaryLoader {

  private static final int WORD_LENGTH = 5;

  public WordleDictionary loadDict(String filename, PrintWriter log) {
    if (log != null) {
      log.println(
          "[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) + " "
              + WordleDictionaryLoader.class.getSimpleName() + "-->"
              + Thread.currentThread().getStackTrace()[1].getMethodName() + "]"
              + " загрузка словаря, файл: " + filename);
    }

    try {
      ArrayList<String> words = readFile(filename);
      return new WordleDictionary(words);
    } catch (IOException e) {
      if (log != null) {
        log.println(
            "[" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
                + " "
                + WordleDictionaryLoader.class.getSimpleName() + "-->"
                + Thread.currentThread().getStackTrace()[1].getMethodName() + "]"
                + " Ошибка загрузки словаря из файла:  " + e.getMessage());
      }
      throw new DictionaryLoadException("Ошибка загрузки словаря из файла: " + filename, e);
    }
  }


  public ArrayList<String> readFile(String filename) throws IOException {

    try (BufferedReader fileReader = new BufferedReader(
        new FileReader(filename, StandardCharsets.UTF_8))) {
      ArrayList<String> words = new ArrayList<>();
      String targetWord;

      while ((targetWord = fileReader.readLine()) != null) {
        targetWord = normalizeWord(targetWord);
        if (targetWord.length() == WORD_LENGTH) {
          words.add(targetWord);
        }
      }
      return words;
    }
  }


  public String normalizeWord(String word) {
    if (word == null) {
      return "";
    }
    return word.toLowerCase().trim().replace('ё', 'е');
  }
}

