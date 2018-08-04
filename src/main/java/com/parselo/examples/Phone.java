package com.parselo.examples;

import java.time.LocalDate;

import com.parselo.annotations.ParseloColumn;
import com.parselo.annotations.ParseloRow;

@ParseloRow(start = 4, end = 6)
public class Phone {

  @ParseloColumn(name = "B")
  private String model;

  @ParseloColumn(name = "C")
  private String brand;

  @ParseloColumn(name = "D")
  private LocalDate boughtOn;

  public String getModel() {
    return model;
  }

  public String getBrand() {
    return brand;
  }

  public LocalDate getBoughtOn() {
    return boughtOn;
  }

  @Override
  public String toString() {
    return "Phone{" +
        "model='" + model + '\'' +
        ", brand='" + brand + '\'' +
        ", boughtOn=" + boughtOn +
        '}';
  }
}
