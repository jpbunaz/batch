package io.balteusit.framework.batch.core.exception;

public class ReaderException extends RuntimeException {

  public ReaderException() {
    super();
  }

  public ReaderException(String message) {
    super(message);
  }

  public ReaderException(String message, Throwable cause) {
    super(message, cause);
  }

  public ReaderException(Throwable cause) {
    super(cause);
  }

  protected ReaderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
