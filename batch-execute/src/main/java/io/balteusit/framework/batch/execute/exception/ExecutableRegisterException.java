package io.balteusit.framework.batch.execute.exception;

public class ExecutableRegisterException extends RuntimeException {

  public ExecutableRegisterException() {
  }

  public ExecutableRegisterException(String message) {
    super(message);
  }

  public ExecutableRegisterException(String message, Throwable cause) {
    super(message, cause);
  }

  public ExecutableRegisterException(Throwable cause) {
    super(cause);
  }

  public ExecutableRegisterException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
