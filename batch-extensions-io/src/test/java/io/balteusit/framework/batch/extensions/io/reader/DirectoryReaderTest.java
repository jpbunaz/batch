package io.balteusit.framework.batch.extensions.io.reader;

import static org.assertj.core.api.Assertions.assertThat;

import io.balteusit.framework.batch.core.Env;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DirectoryReaderTest {

  @DisplayName("Read directory content")
  @Test
  public void readDirectory() {
    String directory = this.getClass().getResource("/directoryfortest").getFile();
    DirectoryReader directoryReader = new DirectoryReader(directory, false);

    testAndAssert(directoryReader, "file1.txt", "file2.txt", "directory1", "directory2");
  }

  @DisplayName("Read directory recursively")
  @Test
  public void readDirectoryRecursively() {
    String directory = this.getClass().getResource("/directoryfortest").getFile();
    DirectoryReader directoryReader = new DirectoryReader(directory, true);

    testAndAssert(directoryReader, "file1.txt", "file2.txt", "directory1", "directory2", "directory21", "file21.txt", "file11.txt");
  }

  @DisplayName("Read directory from session env")
  @Test
  public void readDirectoryFromSession() {

  }

  private void testAndAssert(DirectoryReader directoryReader, String... values) {
    Env env = new Env();
    List<String> results = new ArrayList<>();
    for (int i = 0; i < values.length; i++) {
      Optional<File> read = directoryReader.read(env);
      assertThat(read).isPresent();
      results.add(read.get().getName());
      System.out.println(read);
    }
    assertThat(directoryReader.read(env)).isNotPresent();

    assertThat(results).contains(values);
  }

}