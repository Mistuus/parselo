package com.parselo.utilities;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ColumnIndexCalculatorTests {

  @Test
  void computeIndexOfChar() {
    assertThat(ColumnIndexCalculator.charIndex('A')).isEqualTo(0);
    assertThat(ColumnIndexCalculator.charIndex('D')).isEqualTo(3);
    assertThat(ColumnIndexCalculator.charIndex('Z')).isEqualTo(25);

  }

  @Test
  void singleLetterColumn() {
    assertThat(ColumnIndexCalculator.compute("AA")).isEqualTo(26);
    assertThat(ColumnIndexCalculator.compute("AZ")).isEqualTo(51);
    assertThat(ColumnIndexCalculator.compute("SZ")).isEqualTo(519);
    assertThat(ColumnIndexCalculator.compute("CUZ")).isEqualTo(2_599);
    assertThat(ColumnIndexCalculator.compute("NTQ")).isEqualTo(10_000);
  }

}