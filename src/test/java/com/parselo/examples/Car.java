package com.parselo.examples;

import com.parselo.annotations.ParseloColumn;
import com.parselo.annotations.ParseloRow;
import com.parselo.domain.ConfigurationType;

@ParseloRow(type = ConfigurationType.STATIC, start = 3, end = 5)
public class Car {

  @ParseloColumn(name = "B")
  private String producer;

  @ParseloColumn(name = "C")
  private String model;

  @ParseloColumn(name = "D")
  private Integer year;

  @ParseloColumn(name = "E")
  private Double milleage;

  public Car() {
  }

  public String getProducer() {
    return producer;
  }

  public String getModel() {
    return model;
  }

  public int getYear() {
    return year;
  }

  public Double getMilleage() {
    return milleage;
  }

  @Override
  public String toString() {
    return "Car{" +
        "producer='" + producer + '\'' +
        ", model='" + model + '\'' +
        ", year=" + year +
        ", milleage=" + milleage +
        '}';
  }
}
