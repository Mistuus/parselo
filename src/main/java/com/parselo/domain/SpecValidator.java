package com.parselo.domain;

import org.joda.beans.JodaBeanUtils;

/**
 * Utility class for validating specification fields.
 */
class SpecValidator {

  /**
   * Check whether number is a positive integer.
   *
   * @param value the value to check
   * @param propertyName the name of the property the value represents
   */
  static void isValidRowNumber(int value, String propertyName) {
    if (value < 0) {
      throw new IllegalArgumentException(propertyName + " expected to be a positive number but was '" + value + "'");
    }
  }

  /**
   * Check whether the value represents a valid excel column. Eg: 'A', 'F', 'aD', 'Zsd', 'zzz'
   *
   * @param value the value to check
   * @param propertyName the property name that this value represents
   */
  static void isExcelColumn(String value, String propertyName) {
    JodaBeanUtils.notBlank(value, propertyName);
    if (!value.matches("[a-zA-Z]*")) {
      throw new IllegalArgumentException(propertyName + " expected to be a valid column name (e.g. 'A', 'bb', 'ZAA') " +
          "but was '" + value + "'");
    }
  }
}
