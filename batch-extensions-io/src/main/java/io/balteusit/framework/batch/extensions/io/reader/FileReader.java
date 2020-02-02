package io.balteusit.framework.batch.extensions.io.reader;

import io.balteusit.framework.batch.core.Env;
import io.balteusit.framework.batch.core.exception.ReaderException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;

public class FileReader extends InputStreamReader {

  private File file;

  public FileReader() {
    super();
  }

  public FileReader(String fileName) {
    this.file = new File(fileName);
  }

  public FileReader(String fileName, String encoding) {
    this.file = new File(fileName);
    this.encoding = encoding;
  }

  public FileReader(File file) {
    this.file = file;
  }

  public FileReader(File file, String encoding) {
    this.file = file;
    this.encoding = encoding;
  }

  @Override
  public Optional<String> read(Env env) {
    if (this.bufferedReader == null) {
      if (file == null) {
        file = env.getFile().orElseThrow(() -> new ReaderException("No file found in session"));
      }
      try {
        this.inputStream = new FileInputStream(file);
      } catch (FileNotFoundException e) {
        throw new ReaderException(String.format("File not found %s", file), e);
      }
    }
    return super.read(env);
  }
}
