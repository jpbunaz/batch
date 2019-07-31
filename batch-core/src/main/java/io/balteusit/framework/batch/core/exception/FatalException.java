package io.balteusit.framework.batch.core.exception;

public class FatalException extends RuntimeException {

  public FatalException() {
  }

  public FatalException(String message) {
    super(message);
  }

  public FatalException(String message, Throwable cause) {
    super(message, cause);
  }

  public FatalException(Throwable cause) {
    super(cause);
  }

  public FatalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
