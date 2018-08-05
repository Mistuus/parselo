package com.parselo.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.parselo.examples.DynamicCar;
import com.parselo.examples.NoClassAnnotation;
import com.parselo.examples.NoFieldAnnotation;
import com.parselo.exception.InvalidConfigurationException;

public class ParseloInvalidConfigurationTests {

  private static Parselo parselo;

  @BeforeAll
  static void setup() {
    parselo = Parselo.of("annotation_examples.xls");
  }

  @Test
  public void parse_withNoClassAnnotation_throwsException() {
    Class<NoClassAnnotation> missingClassAnnotationClazz = NoClassAnnotation.class;

    assertThatThrownBy(() -> parselo.parse("Phones", missingClassAnnotationClazz))
        .isInstanceOf(InvalidConfigurationException.class)
        .hasMessage(
            "Class {} is not annotated with Parselo annotations.",
            missingClassAnnotationClazz.getName());
  }

  @Test
  public void parse_withNoFieldAnnotations_throwsException() {
    Class<NoFieldAnnotation> noFieldAnnotationClazz = NoFieldAnnotation.class;

    assertThatThrownBy(() -> parselo.parse("Phones", noFieldAnnotationClazz))
        .isInstanceOf(InvalidConfigurationException.class)
        .hasMessage(
            "Class %s has no field annotations.",
            noFieldAnnotationClazz.getName());
  }

  @Test
  public void parseCar_withMoreFieldsThanColumns_throwsException() {
    ParseloSpec moreColumnsThanFields = ParseloSpec.builder()
        .rowStart(2)
        .rowEnd(5)
        .columnStart("B")
        .columnEnd("I")
        .build();

    assertThatThrownBy(() -> parselo.parse("Cars", DynamicCar.class, moreColumnsThanFields))
        .isInstanceOf(InvalidConfigurationException.class)
        .hasMessage(
            "Expecting nr. of fields: {} to equal nr. of columns: {}",
            DynamicCar.class.getDeclaredFields().length,
            moreColumnsThanFields.columns());
  }

  @Test
  public void parseCar_withLessFieldsThanColumns_throwsException() {
    ParseloSpec moreColumnsThanFields = ParseloSpec.builder()
        .rowStart(2)
        .rowEnd(5)
        .columnStart("B")
        .columnEnd("C")
        .build();

    assertThatThrownBy(() -> parselo.parse("Cars", DynamicCar.class, moreColumnsThanFields))
        .isInstanceOf(InvalidConfigurationException.class)
        .hasMessage(
            "Expecting nr. of fields: {} to equal nr. of columns: {}",
            DynamicCar.class.getDeclaredFields().length,
            moreColumnsThanFields.columns());
  }
}
