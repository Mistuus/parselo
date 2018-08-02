package com.parselo.utilities;

import java.util.Locale;

public class ColumnIndexCalculator {

  public static int compute(String column) {
    column = column.toUpperCase(Locale.ENGLISH);
    if (!column.matches("[A-Z]*")) {
      throw new IllegalArgumentException(String.format(
          "Expecting column to be only english alphabet letters. Found '%s'",
          column));
    }

    int position = 0;
    int length = column.length();
    for (int i = 0; i < length; i++) {
      int charIdx = charIndex(column.charAt(i));
      position += (charIdx + 1) * (int) Math.pow(26, length - i - 1);
    }
    return position - 1;
  }

  static int charIndex(char x) {
    return x - 'A';
  }
}
