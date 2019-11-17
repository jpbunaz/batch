package io.balteusit.framework.batch.execute.exception;

public class NoJobClassExecutableJobException extends RuntimeException {

  public NoJobClassExecutableJobException() {
    super("An existing job class must be define to create an executable job");
  }

}
