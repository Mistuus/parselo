package com.parselo.domain;

/**
 * The type of configuration specified by the Parselo annotations.
 */
public enum ConfigurationType {

  /**
   * Marks a class to parse where the start & end rows as well as the column names of the Excel will be specified on the
   * annotation and will never change.
   */
  STATIC,
  /**
   * Marks a class where the start & end rows as well as the columns are not known upfront and will be specified using
   * a ParseloSpec at the time of parsing.
   */
  DYNAMIC
}
