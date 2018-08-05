package com.parselo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.parselo.examples.DynamicCar;

public class ParseloDynamicAnnotationTest {

  private static Parselo parselo;

  @BeforeAll
  static void setup() {
    parselo = Parselo.of("dynamic_config_examples.xls");
  }

  @Test
  public void parseCar_withDefinedSpec_returnsListOfCars() {
    ParseloSpec spec = ParseloSpec.builder()
        .rowStart(2)
        .rowEnd(5)
        .columnStart("B")
        .columnEnd("E")
        .build();
    List<DynamicCar> cars = parselo.parse("Cars", DynamicCar.class, spec);

    assertThat(cars).hasSize(4);

    DynamicCar opel = cars.get(0);
    assertThat(opel.getModel()).isEqualTo("Astra");
    assertThat(opel.getModel()).isEqualTo("Opel");
    assertThat(opel.getYear()).isEqualTo(2010);
    assertThat(opel.getBoughtOn()).isEqualTo(LocalDate.of(2010, 10, 28));
  }

  @Test
  public void parseCar_withEmptyExcelCells_returnsCarsWithNullFields() {
    Assertions.fail("To be implemented");
  }
}
