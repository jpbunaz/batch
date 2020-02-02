package io.balteusit.framework.batch.extensions.io.reader;

import io.balteusit.framework.batch.core.Env;
import io.balteusit.framework.batch.core.Reader;
import io.balteusit.framework.batch.core.exception.ReaderException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

public class InputStreamReader implements Reader<String, Env> {

  protected BufferedReader bufferedReader;
  protected InputStream inputStream;
  protected String encoding;

  public InputStreamReader() {
  }

  public InputStreamReader(InputStream inputStream) {
    this.inputStream = inputStream;
  }

  public InputStreamReader(InputStream inputStream, String encoding) {
    this.inputStream = inputStream;
    this.encoding = encoding;
  }

  @Override
  public Optional<String> read(Env env) {
    if (bufferedReader == null) {
      if (inputStream == null) {
        inputStream = env.getInputStream().orElseThrow(() -> new ReaderException("No stream found"));
      }
      if (encoding != null) {
        try {
          bufferedReader = new BufferedReader(new java.io.InputStreamReader(inputStream, encoding));
        } catch (UnsupportedEncodingException e) {
          throw new ReaderException(e);
        }
      } else {
        bufferedReader = new BufferedReader(new java.io.InputStreamReader(inputStream));
      }
    }
    try {
      return Optional.ofNullable(bufferedReader.readLine());
    } catch (IOException e) {
      throw new ReaderException("Error while reading line", e);
    }
  }
}
