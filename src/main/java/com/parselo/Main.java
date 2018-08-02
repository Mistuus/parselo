package com.parselo;

import java.util.List;

import com.parselo.domain.Parselo;

public class Main {

  public static void main(String[] args) {
    // ANNOTATION PARSING
    // static parsing
    List<Car> staticCars = Parselo.STANDARD.parse(sheet, Car.class);
    // dynamic parsing
    List<Car> dynamicCars2 = Parselo.STANDARD.parse(sheet, Car.class, rowStart, rowEnd, cellStart, cellEnd);
      // OR...
    ParseloSpec spec = Parselo.SPEC.builder()
        .rowStart(0)
        .rowEnd(10)
        .cellStart("A")
        .cellEnd("Z")
        .build();
    List<Car> dynamicCarsWithSpec = Parselo.STANDARD.parse(sheet, Car.class, spec);

    // LIST PARSING
    List<String> strings = Parselo.STANDARD.parseStrings(sheet, rowStart, rowEnd, cellStart, cellEnd);
    List<Double> doubles = Parselo.STANDARD.parseDoubles(sheet, rowStart, rowEnd, cellStart, cellEnd);
    // OR...
    ParseloSpec spec = Parselo.SPEC.builder()
        .rowStart(0)
        .rowEnd(10)
        .cellStart("A")
        .cellEnd("Z")
        .build();
    ParseloMatrix<Double> strMatrix = Parselo.STANDARD.parseStrings(sheet, spec);
    ParseloMatrix<Double> dblMatrix = Parselo.STANDARD.parseDoubles(sheet, spec);

    // MATRIX PARSING
    Matrix<String> stringMatrix = Parselo.STANDARD.parseStringMatrix(sheet, rowStart, rowEnd, cellStart, cellEnd);
    Matrix<Double> doubleMatrix = Parselo.STANDARD.parseDoubleMatrix(sheet, rowStart, rowEnd, cellStart, cellEnd);
      // OR...
    ParseloSpec spec = Parselo.SPEC.builder()
        .rowStart(0)
        .rowEnd(10)
        .cellStart("A")
        .cellEnd("Z")
        .build();
    ParseloMatrix<Double> strMatrix = Parselo.STANDARD.parseStringMatrix(sheet, spec);
    ParseloMatrix<Double> dblMatrix = Parselo.STANDARD.parseDoubleMatrix(sheet, spec);

    //----------------------------------------------------------
    // CUSTOM PARSING
    Parselo customParselo = Parselo.CUSTOM.builder()
        .exceptionHandler(Consumer<ParseloFailure> exceptionConsumer)
        .defaultForString()
        .defaultForDouble()
        .defaultForDate()
        .build();

    // use customParselo in the same way as the standard one
  }

}
