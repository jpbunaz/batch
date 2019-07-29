package io.balteusit.framework.batch.core.exception;

public class MaximumSkipException extends RuntimeException {

  public MaximumSkipException() {
  }

  public MaximumSkipException(String message) {
    super(message);
  }

  public MaximumSkipException(String message, Throwable cause) {
    super(message, cause);
  }

  public MaximumSkipException(Throwable cause) {
    super(cause);
  }

  public MaximumSkipException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
