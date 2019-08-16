package io.balteusit.framework.batch.core.util;

import io.balteusit.framework.batch.core.BatchProperties;
import io.balteusit.framework.batch.core.exception.DateFormatException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

  public static Date toDate(String dateAsString) {
    String dateFormat = BatchProperties.getInstance().getProperty("default.dateformat", "yyyyMMddHHmmss");
    return toDate(dateAsString, dateFormat);
  }

  public static Date toDate(String dateAsString, String dateFormat) {
    try {
      return new SimpleDateFormat(dateFormat).parse(dateAsString);
    } catch (ParseException e) {
      throw new DateFormatException(dateAsString, dateFormat);
    }
  }


}
