package io.balteusit.framework.batch.core.util;

import java.util.Random;

public class StringUtils {

  public static String random(int length) {
    int min = 97;
    int max = 122;
    StringBuffer buffer = new StringBuffer();
    Random random = new Random();
    for (int i = 0; i < length; i++) {
      int generatedInt = random.nextInt(max - min) + min;
      char c = (char) generatedInt;
      buffer.append(c);
    }
    return buffer.toString();
  }

  public static boolean isNullOrEmpty(String string) {
    return !isNotNullOrEmpty(string);
  }

  public static boolean isNotNullOrEmpty(String string) {
    return string != null && string.trim().length() > 0;
  }

}
