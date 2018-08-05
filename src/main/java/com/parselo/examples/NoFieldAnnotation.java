package com.parselo.examples;

import java.time.LocalDate;

import com.parselo.annotations.Parselo;

@Parselo
public class NoFieldAnnotation {

  private String field;

  private LocalDate dateField;

  public NoFieldAnnotation() {
  }

  public String getField() {
    return field;
  }

  public LocalDate getDateField() {
    return dateField;
  }
}
