package io.balteusit.framework.batch.core;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;

public class Env {

  public static final String BATCH_FILE = "batch.file";
  public static final String BATCH_INPUTSTREAM = "batch.inputstream";
  public static final String BATCH_COLLECTION = "batch.collection";

  private Map<String, Object> sessions = new HashMap<>();

  private Logger logger;

  public Env() {

  }

  public Optional<Object> getSession(String key) {
    return Optional.ofNullable(sessions.get(key));
  }

  @SuppressWarnings("unchecked")
  public <T> Optional<T> getSession(String key, Class<T> zClass) {
    T o = (T) sessions.get(key);
    return Optional.ofNullable(o);
  }

  @SuppressWarnings("unchecked")
  public <T> Collection<T> getSessionCollection(String key, Class<T> zClass) {
    Collection<T> o = (Collection<T>) sessions.get(key);
    if (o == null) {
      return Collections.emptyList();
    }
    return o;
  }

  public void setSession(String key, Object object) {
    sessions.put(key, object);
  }

  public Logger getLogger() {
    return logger;
  }

  public void setLogger(Logger logger) {
    this.logger = logger;
  }

  public Optional<File> getFile() {
    return this.getSession(BATCH_FILE, File.class);
  }

  public void setFile(File file) {
    this.setSession(BATCH_FILE, file);
  }

  public Optional<InputStream> getInputStream() {
    return this.getSession(BATCH_FILE, InputStream.class);
  }

  public void setInputStream(InputStream inputStream) {
    this.setSession(BATCH_INPUTSTREAM, inputStream);
  }

  public <T> Collection<T> getCollection(Class<T> zClass) {
    return this.getSessionCollection(BATCH_COLLECTION, zClass);
  }

}
