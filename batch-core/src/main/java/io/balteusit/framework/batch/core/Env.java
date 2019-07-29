package io.balteusit.framework.batch.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Env {

  private Map<String, String> parameters;

  @SuppressWarnings("unchecked")
  private Map<String, Object> sessions = new HashMap();

  private int currentLine = 0;

  private final Logger logger;

  public Env(Map<String, String> parameters, Class loggerClass) {
    this.parameters = parameters;
    this.logger = LoggerFactory.getLogger(loggerClass);
  }

  public Env(Map<String, String> parameters, String loggerName) {
    this.parameters = parameters;
    this.logger = LoggerFactory.getLogger(loggerName);
  }

  public Optional<String> getParameter(String key) {
    return Optional.ofNullable(parameters.get(key));
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

  public int getCurrentLine() {
    return currentLine;
  }

  public void setCurrentLine(int currentLine) {
    this.currentLine = currentLine;
  }

  public static void main(String[] args) {
    System.out.println(Env.class.getPackage().getName());
  }

}
