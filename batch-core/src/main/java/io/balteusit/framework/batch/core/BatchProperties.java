package io.balteusit.framework.batch.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import io.balteusit.framework.batch.core.exception.FatalException;

public class BatchProperties extends Properties {

  private static BatchProperties batchProperties = new BatchProperties();

  private BatchProperties() {
    loadProperties();
  }

  private void loadProperties() {
    loadFromClasspath();
  }

  private void loadFromClasspath() {
    try (InputStream inputStream = BatchProperties.class.getClassLoader().getResourceAsStream("batch.properties")) {
      this.load(inputStream);
    } catch (IOException e) {
      throw new FatalException("Error while loading batch.properties file");
    }
  }

  public static BatchProperties getInstance() {
    return batchProperties;
  }

}
