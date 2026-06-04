package ru.yandex.practicum;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class WordleDictionaryTest {

  private WordleDictionary dictionary;

  @BeforeEach
  void setUp() {
    WordleDictionaryLoader loader = new WordleDictionaryLoader();
    StringWriter stringWriter = new StringWriter();
    PrintWriter log = new PrintWriter(stringWriter);
    dictionary = loader.loadDict("test_words.txt", log);
  }

  @Test
  void testGetWords() {
    List<String> words = dictionary.getWords();
    assertEquals(13, words.size());
    assertTrue(words.contains("домой"));
  }

  @Test
  void testContainsWord() {
    assertTrue(dictionary.containsWord("домой"));
    assertTrue(dictionary.containsWord("ДОМОЙ"));
    assertTrue(dictionary.containsWord("Домой"));
    assertFalse(dictionary.containsWord("абвгд"));
    assertFalse(dictionary.containsWord(null));
  }

  @Test
  void testGetRandomWord() {
    String word = dictionary.getRandomWord();
    assertNotNull(word);
    assertEquals(5, word.length());
  }

  @Test
  void testNormalizeWord() {
    assertEquals("елка", dictionary.normalizeWord("ёлка"));
    assertEquals("елка", dictionary.normalizeWord("ЕЛКА"));
    assertEquals("", dictionary.normalizeWord(null));
  }

  @Test
  void testGetFilteredWords_EmptyFilters() {
    Map<Character, Set<Integer>> correctPositions = new HashMap<>();
    Set<Character> containsLetters = new HashSet<>();
    Set<Character> excludedLetters = new HashSet<>();

    List<String> filtered = dictionary.getFilteredWords(correctPositions, containsLetters,
        excludedLetters);
    assertEquals(13, filtered.size());
  }

  @Test
  void testGetFilteredWords_FilterByCorrectPosition() {
    Map<Character, Set<Integer>> correctPositions = new HashMap<>();
    correctPositions.put('д', new HashSet<>(List.of(0)));

    Set<Character> containsLetters = new HashSet<>();
    Set<Character> excludedLetters = new HashSet<>();

    List<String> filtered = dictionary.getFilteredWords(correctPositions, containsLetters,
        excludedLetters);
    assertTrue(filtered.contains("домой"));
    assertTrue(filtered.contains("дачка"));
    assertFalse(filtered.contains("метро"));
    assertFalse(filtered.contains("округ"));
  }

  @Test
  void testGetFilteredWords_FilterByContainsLetters() {
    Map<Character, Set<Integer>> correctPositions = new HashMap<>();
    Set<Character> containsLetters = new HashSet<>(Arrays.asList('о', 'д'));
    Set<Character> excludedLetters = new HashSet<>();

    List<String> filtered = dictionary.getFilteredWords(correctPositions, containsLetters,
        excludedLetters);
    assertTrue(filtered.contains("домой"));
    assertTrue(filtered.contains("дождь"));
    assertFalse(filtered.contains("погода"));
    assertFalse(filtered.contains("округ"));
  }

  @Test
  void testGetFilteredWords_FilterByExcludedLetters() {
    Map<Character, Set<Integer>> correctPositions = new HashMap<>();
    Set<Character> containsLetters = new HashSet<>();
    Set<Character> excludedLetters = new HashSet<>(Arrays.asList('о', 'д'));

    List<String> filtered = dictionary.getFilteredWords(correctPositions, containsLetters,
        excludedLetters);
    assertFalse(filtered.contains("домой"));
    assertFalse(filtered.contains("дождь"));
    assertFalse(filtered.contains("погода"));
    assertFalse(filtered.contains("столы"));
    assertFalse(filtered.contains("метро"));
    assertTrue(filtered.contains("книга"));
    assertTrue(filtered.contains("ручка"));
  }

  @Test
  void testGetFilteredWords_CombinedFilters() {
    Map<Character, Set<Integer>> correctPositions = new HashMap<>();
    correctPositions.put('о', new HashSet<>(List.of(0)));

    Set<Character> containsLetters = new HashSet<>(List.of('г'));
    Set<Character> excludedLetters = new HashSet<>(List.of('д'));

    List<String> filtered = dictionary.getFilteredWords(correctPositions, containsLetters,
        excludedLetters);
    assertTrue(filtered.contains("округ"));
    assertFalse(filtered.contains("домой"));
  }

  @Test
  void testGetFilteredWords_NoMatches() {
    Map<Character, Set<Integer>> correctPositions = new HashMap<>();
    correctPositions.put('я', new HashSet<>(List.of(0)));

    Set<Character> containsLetters = new HashSet<>();
    Set<Character> excludedLetters = new HashSet<>();

    List<String> filtered = dictionary.getFilteredWords(correctPositions, containsLetters,
        excludedLetters);
    assertTrue(filtered.isEmpty());
  }
}
