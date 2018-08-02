package com.parselo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.parselo.domain.Parselo;
import com.parselo.domain.ParseloSpec;

class ParseloTest {

  private static Parselo parselo;

  @BeforeAll
  static void setup() {
    parselo = Parselo.of("test_examples.xls");
  }

  @Test
  void createFromFilename() {
    assertThat(parselo).isNotNull();
  }

  @Test
  void getAllSheetNames() {
    List<String> sheetNames = parselo.getSheetNames();
    assertThat(sheetNames).hasSize(1);
    assertThat(sheetNames.get(0)).isEqualToIgnoringCase("String Array");
  }

  @Test
  void parseHorizontalStringArray() {
    ParseloSpec spec = ParseloSpec.builder()
        .rowStart(1)
        .rowEnd(1)
        .columnStart("B")
        .columnEnd("E")
        .build();
    List<String> parsed = parselo.parseStringArray("String Array", spec);

    assertThat(parsed).hasSize(4);
    assertThat(parsed).containsExactly("a", "b", "c", "d");
  }

  @Test
  void parseVerticalStringArray() {
    ParseloSpec spec = ParseloSpec.builder()
        .rowStart(5)
        .rowEnd(8)
        .columnStart("B")
        .columnEnd("B")
        .build();

    List<String> parsed = parselo.parseStringArray("String Array", spec);
    assertThat(parsed).hasSize(4);
    assertThat(parsed).containsExactly("e", "f", "g", "h");
  }

}