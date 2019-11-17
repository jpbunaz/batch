package io.balteusit.framework.batch.execute.exception;

public class NotExistingJobClassExecutableJobException extends RuntimeException {

  public static final String MESSAGE = "A valid Job must be define to create an executable job";

  public NotExistingJobClassExecutableJobException() {
    super(MESSAGE);
  }

  public NotExistingJobClassExecutableJobException(Throwable cause) {
    super(MESSAGE, cause);
  }
}
