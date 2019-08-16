package io.balteusit.framework.batch.execute.exception;

public class RemoveExecutableException extends RuntimeException {

  public static String MESSAGE_RUNNING_EXECUTION = "Executable can't be deleted during an execution";

  public static String MESSAGE_EXISTING_EXECUTION = "Delete all execution or use hard mode";

  public RemoveExecutableException() {
    super();
  }

  public RemoveExecutableException(String message) {
    super(message);
  }

  public RemoveExecutableException(String message, Throwable cause) {
    super(message, cause);
  }

  public RemoveExecutableException(Throwable cause) {
    super(cause);
  }

  protected RemoveExecutableException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
