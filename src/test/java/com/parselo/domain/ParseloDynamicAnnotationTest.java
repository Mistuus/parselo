package com.parselo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.parselo.examples.DynamicCar;

public class ParseloDynamicAnnotationTest {

  private static Parselo parselo;

  @BeforeAll
  static void setup() {
    parselo = Parselo.of("annotation_examples.xls");
  }

  @Test
  public void parseCar_withDefinedSpec_returnsListOfCars() {
    ParseloSpec spec = ParseloSpec.builder()
        .rowStart(3)
        .rowEnd(5)
        .columnStart("B")
        .columnEnd("E")
        .build();
    List<DynamicCar> cars = parselo.parse("Cars", DynamicCar.class, spec);

    assertThat(cars).hasSize(3);

    DynamicCar opel = cars.get(0);
    assertThat(opel.getType()).isEqualTo("Astra");
    assertThat(opel.getProducer()).isEqualTo("Opel");
    assertThat(opel.getYear()).isEqualTo(2010);
    assertThat(opel.getMilleage()).isEqualTo(10_000);
  }

  @Test
  public void parseCar_withEmptyExcelCells_returnsCarsWithNullFields() {
    ParseloSpec spec = ParseloSpec.builder()
        .rowStart(10)
        .rowEnd(11)
        .columnStart("B")
        .columnEnd("E")
        .build();

    List<DynamicCar> cars = parselo.parse("Cars", DynamicCar.class, spec);

    assertThat(cars).hasSize(2);

    DynamicCar mercedes = cars.get(0);
    assertThat(mercedes.getProducer()).isEqualTo("Mercedes");
    assertThat(mercedes.getType()).isNull();
    assertThat(mercedes.getYear()).isEqualTo(2000);
    assertThat(mercedes.getMilleage()).isEqualTo(0);

    DynamicCar ford = cars.get(1);
    assertThat(ford.getProducer()).isNull();
    assertThat(ford.getType()).isEqualTo("Fiesta");
    assertThat(ford.getYear()).isEqualTo(2001);
    assertThat(ford.getMilleage()).isEqualTo(1);
  }
}
