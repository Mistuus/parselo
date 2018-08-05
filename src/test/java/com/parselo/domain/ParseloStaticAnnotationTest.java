package com.parselo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.parselo.examples.Car;
import com.parselo.examples.Phone;

class ParseloStaticAnnotationTest {

  private static Parselo parselo;

  @BeforeAll
  static void setup() {
    parselo = Parselo.of("annotation_examples.xls");
  }


  @Test
  void parse_withAnnotatedCar_returnsListOfCars() {
    List<Car> parsedCars = parselo.parse("Cars", Car.class);

    assertThat(parsedCars).hasSize(3);

    Car opel = parsedCars.get(0);
    assertThat(opel.getProducer()).isEqualToIgnoringCase("Opel");
    assertThat(opel.getModel()).isEqualTo("Astra");
    assertThat(opel.getYear()).isEqualTo(2010);
    assertThat(opel.getMilleage()).isEqualTo(10_000);
  }

  @Test
  void parse_withAnnotatedPhone_returnsListOfPhonesWithNullValues() {
    List<Phone> phones = parselo.parse("Phones", Phone.class);

    assertThat(phones).hasSize(3);

    Phone iphone = phones.get(0);
    assertThat(iphone.getModel()).isEqualTo("iPhone 8");
    assertThat(iphone.getBrand()).isEqualTo("Apple");
    assertThat(iphone.getBoughtOn()).isNull();

    Phone samsung = phones.get(1);
    assertThat(samsung.getModel()).isNull();
    assertThat(samsung.getBrand()).isEqualTo("Samsung");
    assertThat(samsung.getBoughtOn()).isEqualTo(LocalDate.of(2017, 8, 10));

    Phone onePlus = phones.get(2);
    assertThat(onePlus.getModel()).isEqualTo("OnePlus");
    assertThat(onePlus.getBrand()).isNull();
    assertThat(onePlus.getBoughtOn()).isEqualTo(LocalDate.of(2019, 3, 4));
  }

}
