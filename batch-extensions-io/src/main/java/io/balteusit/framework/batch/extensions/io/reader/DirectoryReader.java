package io.balteusit.framework.batch.extensions.io.reader;

import static java.util.Objects.requireNonNull;

import io.balteusit.framework.batch.core.Env;
import io.balteusit.framework.batch.core.Reader;
import io.balteusit.framework.batch.core.exception.ReaderException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;

public class DirectoryReader implements Reader<File, Env> {

  private File directory;
  private Iterator<File> iterator;
  private boolean recursively;

  public DirectoryReader() {

  }

  public DirectoryReader(boolean recursively) {
    this.recursively = recursively;
  }

  public DirectoryReader(String directory, boolean recursively) {
    this.directory = new File(directory);
    this.recursively = recursively;
    checkIfDirectory();
  }

  @Override
  public Optional<File> read(Env env) {
    if (directory == null) {
      this.directory = env.getFile().orElseThrow(() -> new ReaderException("No directory found"));
      checkIfDirectory();
    }
    loadIterator();
    if (iterator.hasNext()) {
      return Optional.of(iterator.next());
    }
    return Optional.empty();
  }

  private void loadIterator() {
    if (iterator == null) {
      if (this.recursively) {
        try {
          iterator = Files.walk(this.directory.toPath()).map(Path::toFile).skip(1).iterator();
        } catch (IOException e) {
          throw new ReaderException(e);
        }
      } else {
        iterator = Arrays.asList(requireNonNull(this.directory.listFiles())).iterator();
      }
    }
  }

  private void checkIfDirectory() {
    if (!this.directory.isDirectory()) {
      throw new ReaderException(String.format("%s is not a directory", directory));
    }
  }

}
