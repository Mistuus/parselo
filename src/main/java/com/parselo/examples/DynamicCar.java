package com.parselo.examples;

import java.time.LocalDate;

import com.parselo.annotations.Parselo;
import com.parselo.annotations.ParseloPosition;

@Parselo
public class DynamicCar {

  @ParseloPosition(position = 0)
  private String model;

  @ParseloPosition(position = 1)
  private String producer;

  @ParseloPosition(position = 2)
  private Integer year;

  @ParseloPosition(position = 3)
  private LocalDate boughtOn;


  public DynamicCar() {
  }

  public String getModel() {
    return model;
  }

  public String getProducer() {
    return producer;
  }

  public Integer getYear() {
    return year;
  }

  public LocalDate getBoughtOn() {
    return boughtOn;
  }

  @Override
  public String toString() {
    return "DynamicCar{" +
        "model='" + model + '\'' +
        ", producer='" + producer + '\'' +
        ", year=" + year +
        ", boughtOn=" + boughtOn +
        '}';
  }
}
