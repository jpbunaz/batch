package io.balteusit.framework.batch.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;

public class Env {

  private Map<String, Object> sessions = new HashMap<>();

  private Logger logger;

  public Env() {

  }

  public Optional<Object> getSession(String key) {
    return Optional.ofNullable(sessions.get(key));
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
}
