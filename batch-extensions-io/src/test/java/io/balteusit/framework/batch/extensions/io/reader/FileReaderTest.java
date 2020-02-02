package io.balteusit.framework.batch.extensions.io.reader;

import static org.assertj.core.api.Assertions.assertThat;

import io.balteusit.framework.batch.core.Env;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileReaderTest {

  private List<String> fileLines = new ArrayList<>();

  {
    fileLines.add("Ce texte ne veut rien mais il");
    fileLines.add("doit être sur plusieurs lignes et");
    fileLines.add("il contient des accents pour être certains");
    fileLines.add("de bien pouvoir tester l'encodage du fichier");
  }

  @Test
  @DisplayName("Read an UTF8 File")
  public void testUtf8File() {
    String file = this.getClass().getResource("/test_utf8.txt").getFile();
    FileReader fileReader = new FileReader(file);
    testAndAssert(fileReader, new Env());
  }

  @Test
  @DisplayName("Read an iso-8859-1 File")
  public void testIso88591File() {
    String file = this.getClass().getResource("/test_iso88591.txt").getFile();
    FileReader fileReader = new FileReader(file, "iso-8859-1");
    testAndAssert(fileReader, new Env());
  }

  @Test
  @DisplayName("Read a file from session")
  public void testFileFromSession() {
    String fileName = this.getClass().getResource("/test_utf8.txt").getFile();
    FileReader fileReader = new FileReader();
    Env env = new Env();
    env.setFile(new File(fileName));
    testAndAssert(fileReader, env);
  }

  private void testAndAssert(FileReader fileReader, Env env) {
    for (int i = 0; i < 4; i++) {
      assertThat(fileReader.read(env)).isPresent().get().isEqualTo(fileLines.get(i));
    }
    assertThat(fileReader.read(env)).isNotPresent();
  }

}