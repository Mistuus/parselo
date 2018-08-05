package com.parselo.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ParseloTest {

  private static Parselo parselo;

  @BeforeAll
  static void setup() {
    parselo = Parselo.of("test_examples.xls");
  }

  @Test
  void parselo_fromFilename_isNotNullObject() {
    assertThat(parselo).isNotNull();
  }

  @Test
  void getAllSheetNames_returnsSheetNames() {
    List<String> sheetNames = parselo.getSheetNames();
    assertThat(sheetNames).hasSize(2);
    assertThat(sheetNames).containsExactly("String Array", "String Matrix");
  }

  @Test
  void parse_horizontalStringList_returnsArray() {
    ParseloSpec spec = ParseloSpec.builder()
        .rowStart(1)
        .rowEnd(1)
        .columnStart("B")
        .columnEnd("E")
        .build();
    List<String> parsed = parselo.parseList("String Array", CellConverters.TO_STRING, spec);

    assertThat(parsed).hasSize(4);
    assertThat(parsed).containsExactly("a", "b", "c", "d");
  }

  @Test
  void parse_verticalStringList_returnList() {
    ParseloSpec spec = ParseloSpec.builder()
        .rowStart(5)
        .rowEnd(8)
        .columnStart("B")
        .columnEnd("B")
        .build();

    List<String> parsed = parselo.parseList("String Array", CellConverters.TO_STRING, spec);
    assertThat(parsed).hasSize(4);
    assertThat(parsed).containsExactly("e", "f", "g", "h");
  }

  @Test
  void parse_stringMatrix_returnsMatrix() {
    ParseloSpec spec = ParseloSpec.builder()
        .rowStart(1)
        .rowEnd(3)
        .columnStart("B")
        .columnEnd("E")
        .build();

    ParseloMatrix<String> parsed = parselo.parseMatrix("String Matrix", CellConverters.TO_STRING, spec);

    assertThat(parsed).isNotNull();
    assertThat(parsed.rowCount()).isEqualTo(3);
    assertThat(parsed.columnCount()).isEqualTo(4);
    assertThat(parsed.getRow(0)).containsExactly("a", "b", "c", "d");
    assertThat(parsed.getRow(1)).containsExactly("e", "f", "g", "h");
    assertThat(parsed.getRow(2)).containsExactly("i", "j", "k", "l");
  }

  @Test
  void parse_matrixWithEmptyFields_returnsFieldsWithEmptyStrings() {
    ParseloSpec spec = ParseloSpec.builder()
        .rowStart(7)
        .rowEnd(8)
        .columnStart("B")
        .columnEnd("E")
        .build();

    ParseloMatrix<String> parsed = parselo.parseMatrix("String Matrix", CellConverters.TO_STRING, spec);

    assertThat(parsed).isNotNull();
    assertThat(parsed.rowCount()).isEqualTo(2);
    assertThat(parsed.columnCount()).isEqualTo(4);
    assertThat(parsed.getRow(0)).containsExactly("m", "", "n", "o");
    assertThat(parsed.getRow(1)).containsExactly("p", "q", "", "r");
  }
}