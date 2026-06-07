package ru.yandex.practicum;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class WordleDictionaryLoaderTest {

  @TempDir
  Path tempDir;

  @Test
  void testLoadDict_Success() throws IOException {

    Path tempFile = tempDir.resolve("test_words.txt");
    Files.write(tempFile, Arrays.asList("домов", "дачка", "оконце", "столик"));

    WordleDictionaryLoader loader = new WordleDictionaryLoader();
    StringWriter logWriter = new StringWriter();
    PrintWriter log = new PrintWriter(logWriter);

    WordleDictionary dictionary = loader.loadDict(tempFile.toString(), log);

    assertNotNull(dictionary);
    assertEquals(2, dictionary.getWords().size());
    assertTrue(dictionary.containsWord("домов"));
    assertTrue(dictionary.containsWord("дачка"));
  }

  @Test
  void testLoadDict_FileNotFound() {
    WordleDictionaryLoader loader = new WordleDictionaryLoader();
    StringWriter logWriter = new StringWriter();
    PrintWriter log = new PrintWriter(logWriter);

    assertThrows(ru.yandex.practicum.exceptions.DictionaryLoadException.class,
        () -> loader.loadDict("nonexistent_file.txt", log));
  }

  @Test
  void testLoadDict_EmptyFile() throws IOException {
    // Создаём пустой файл
    Path tempFile = tempDir.resolve("empty_words.txt");
    Files.write(tempFile, Collections.emptyList());

    WordleDictionaryLoader loader = new WordleDictionaryLoader();
    StringWriter logWriter = new StringWriter();
    PrintWriter log = new PrintWriter(logWriter);

    assertThrows(ru.yandex.practicum.exceptions.EmptyDictionaryException.class, () -> {
      loader.loadDict(tempFile.toString(), log);
    });
  }

  @Test
  void testLoadDict_NormalizesWords() throws IOException {
    // Создаём файл с разными регистрами
    Path tempFile = tempDir.resolve("test_normalize.txt");
    Files.write(tempFile, Arrays.asList("ДОМОВ", "ДаЧкА", "оКнОЦЕ"));

    WordleDictionaryLoader loader = new WordleDictionaryLoader();
    StringWriter logWriter = new StringWriter();
    PrintWriter log = new PrintWriter(logWriter);

    WordleDictionary dictionary = loader.loadDict(tempFile.toString(), log);

    // Проверяем нормализацию
    assertTrue(dictionary.containsWord("домов"));
    assertTrue(dictionary.containsWord("дачка"));
    assertFalse(dictionary.containsWord("оконце"));
  }

  @Test
  void testLoadDict_TrimsWhitespace() throws IOException {
    // Создаём файл со словами с пробелами
    Path tempFile = tempDir.resolve("test_whitespace.txt");
    Files.write(tempFile, Arrays.asList("  домов ", " дачка", "оконце "));

    WordleDictionaryLoader loader = new WordleDictionaryLoader();
    StringWriter logWriter = new StringWriter();
    PrintWriter log = new PrintWriter(logWriter);

    WordleDictionary dictionary = loader.loadDict(tempFile.toString(), log);

    assertTrue(dictionary.containsWord("домов"));
    assertTrue(dictionary.containsWord("дачка"));
    assertFalse(dictionary.containsWord("оконце"));
  }

  @Test
  void testLoadDict_SkipsEmptyLines() throws IOException {
    // Создаём файл с пустыми строками
    Path tempFile = tempDir.resolve("test_empty_lines.txt");
    Files.write(tempFile, Arrays.asList("домов", "", "дачка", "   ", "оконце"));

    WordleDictionaryLoader loader = new WordleDictionaryLoader();
    StringWriter logWriter = new StringWriter();
    PrintWriter log = new PrintWriter(logWriter);

    WordleDictionary dictionary = loader.loadDict(tempFile.toString(), log);

    assertEquals(2, dictionary.getWords().size());
  }
}
