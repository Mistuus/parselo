package com.parselo.examples;

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
  private Integer milleage;


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

  public Integer getMilleage() {
    return milleage;
  }
}
