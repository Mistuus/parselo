package com.parselo.examples;

import com.parselo.annotations.ParseloPosition;
import com.parselo.annotations.ParseloRow;
import com.parselo.domain.ConfigurationType;

@ParseloRow
public class DynamicCar {

  @ParseloPosition(position = 0)
  private String producer;

  @ParseloPosition(position = 1)
  private String type;

  @ParseloPosition(position = 2)
  private Integer year;

  @ParseloPosition(position = 3)
  private Integer milleage;


  public DynamicCar() {
  }

  public String getType() {
    return type;
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
