package com.parselo.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.parselo.annotations.ParseloColumn;
import com.parselo.annotations.ParseloRow;
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
            "Class %s needs to be annotated with %s",
            missingClassAnnotationClazz.getName(),
            ParseloRow.class.getName());
  }

  @Test
  public void parse_withNoFieldAnnotations_throwsException() {
    Class<NoFieldAnnotation> noFieldAnnotationClazz = NoFieldAnnotation.class;

    assertThatThrownBy(() -> parselo.parse("Phones", noFieldAnnotationClazz))
        .isInstanceOf(InvalidConfigurationException.class)
        .hasMessage(
            "Class %s has no fields annotated with %s",
            noFieldAnnotationClazz.getName(),
            ParseloColumn.class.getName());
  }

  @Test
  public void parseCar_withMoreFieldsThanColumns_throwsException() {
    ParseloSpec moreColumnsThanFields = ParseloSpec.builder()
        .rowStart(2)
        .rowEnd(5)
        .columnStart("B")
        .columnEnd("I") // this is past the number of fields defined
        .build();

    assertThatThrownBy(() -> parselo.parse("Cars", DynamicCar.class, moreColumnsThanFields))
        .isInstanceOf(InvalidConfigurationException.class)
        .hasMessage(
            "Expecting nr. of annotated fields=%d to equal nr. columns from spec=%d",
            DynamicCar.class.getDeclaredFields().length,
            moreColumnsThanFields.columns());
  }

  @Test
  public void parseCar_withLessFieldsThanColumns_throwsException() {
    ParseloSpec moreFieldsThanColumns = ParseloSpec.builder()
        .rowStart(2)
        .rowEnd(5)
        .columnStart("B")
        .columnEnd("C")
        .build();

    assertThatThrownBy(() -> parselo.parse("Cars", DynamicCar.class, moreFieldsThanColumns))
        .isInstanceOf(InvalidConfigurationException.class)
        .hasMessage(
            "Expecting nr. of annotated fields=%d to equal nr. columns from spec=%d",
            DynamicCar.class.getDeclaredFields().length,
            moreFieldsThanColumns.columns());
  }
  
  @Test
  public void parseCar_withMoreRowsThanAvailableInSheet_throwsException() {
    ParseloSpec startRowOutOfBounds = ParseloSpec.builder()
        .rowStart(2)
        .rowEnd(30)
        .columnStart("A")
        .columnEnd("D")
        .build();

    assertThatThrownBy(() -> parselo.parse("Cars", DynamicCar.class, startRowOutOfBounds))
        .isInstanceOf(InvalidConfigurationException.class)
        .hasMessageContaining("rows must be within the bounds");
  }
}
