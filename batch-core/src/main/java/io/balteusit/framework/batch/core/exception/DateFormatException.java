package io.balteusit.framework.batch.core.exception;

public class DateFormatException extends RuntimeException {

  public DateFormatException(String date, String format) {
    super(String.format("Error when parsing date %s with format %s", date, format));
  }

}
