package com.parselo.domain;

import java.util.Locale;
import java.util.regex.Pattern;

import org.joda.beans.JodaBeanUtils;

/**
 * Utilities class for dealing with different excel specific challenges.
 */
class ExcelUtils {

  /**
   * The excel column name regex to check column names against.
   */
  private static final Pattern IS_EXCEL_COLUMN =Pattern.compile("[a-zA-Z]*");

  /**
   * Check whether the value represents a valid excel column. Eg: 'A', 'F', 'aD', 'Zsd', 'zzz'
   *
   * @param value the value to check
   * @param propertyName the property name that this value represents
   */
  static void isExcelColumn(String value, String propertyName) {
    JodaBeanUtils.notBlank(value, propertyName);
    if (!IS_EXCEL_COLUMN.matcher(value).matches()) {
      throw new IllegalArgumentException(propertyName + " expected to be a valid column name (e.g. 'A', 'bb', 'ZAA') " +
          "but was '" + value + "'");
    }
  }

  /**
   * Check whether number represents a valid  excel row index (one-based).
   *
   * @param index the index to check
   * @param propertyName the name of the property the index represents
   */
  static void isValidRowNumber(int index, String propertyName) {
    if (index <= 0) {
      throw new IllegalArgumentException(propertyName + " expected to be a positive non-zero number but was '" + index + "'");
    }
  }

  /**
   * Convert a case insensitive column name (like 'A', 'bb', 'cdA', 'CzA' etc) to the zero-based index of that column.
   *
   * @param column the column name
   * @return the zero-based index of that column name
   * @throws IllegalArgumentException if the column name doesn't contain only alphabet letters
   */
  static int columnIndex(String column) {
    column = column.toUpperCase(Locale.ENGLISH);
    if (!column.matches("[A-Z]*")) {
      throw new IllegalArgumentException(String.format(
          "Expecting column to be only english alphabet letters. Found '%s'",
          column));
    }

    int position = 0;
    int length = column.length();
    for (int i = 0; i < length; i++) {
      int charIdx = column.charAt(i) - 'A';
      position += (charIdx + 1) * (int) Math.pow(26, length - i - 1);
    }
    return position;
  }
}
