package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import ru.yandex.practicum.exceptions.EmptyDictionaryException;


public class WordleDictionary {

  private final List<String> words;

  public WordleDictionary(List<String> words) {
    if (words == null || words.isEmpty()) {
      throw new EmptyDictionaryException("Словарь не может быть пустым!");
    }
    this.words = new ArrayList<>(words);
  }

  public List<String> getWords() {
    return words;
  }

  public boolean containsWord(String word) {
    if (word == null) {
      return false;
    }
    return words.contains(normalizeWord(word));
  }

  public String getRandomWord() {
    if (words == null || words.isEmpty()) {
      throw new EmptyDictionaryException("Случайное слово не получить, словарь пуст!");
    }
    Random random = new Random();
    int randomIndex = random.nextInt(words.size());
    return words.get(randomIndex);
  }

  public String normalizeWord(String word) {
    if (word == null) {
      return "";
    }
    return word.toLowerCase().replace('ё', 'е');
  }

  public List<String> getFilteredWords(Map<Character, Set<Integer>> correctPositions,
      Set<Character> containsLetters, Set<Character> excludedLetters) {
    List<String> filtered = new ArrayList<>();

    for (String word : words) {
      boolean valid = true;

      // Проверяем буквы на правильных позициях
      for (Map.Entry<Character, Set<Integer>> entry : correctPositions.entrySet()) {
        char letter = entry.getKey();
        for (int position : entry.getValue()) {
          if (position >= word.length() || word.charAt(position) != letter) {
            valid = false;
            break;
          }
        }
        if (!valid) {
          break;
        }
      }

      if (!valid) {
        continue;
      }

      for (char letter : containsLetters) {
        if (!word.contains(String.valueOf(letter))) {
          valid = false;
          break;
        }
      }

      if (!valid) {
        continue;
      }

      for (char letter : excludedLetters) {
        if (word.contains(String.valueOf(letter))) {
          valid = false;
          break;
        }
      }

      if (valid) {
        filtered.add(word);
      }
    }
    return filtered;
  }

}
