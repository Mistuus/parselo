package com.parselo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ParseloSpecTest {

  @Test
  void computeIndexOfChar() {
    assertThat(ParseloSpec.charIndex('A')).isEqualTo(0);
    assertThat(ParseloSpec.charIndex('D')).isEqualTo(3);
    assertThat(ParseloSpec.charIndex('Z')).isEqualTo(25);

  }

  @Test
  void singleLetterColumn() {
    assertThat(ParseloSpec.compute("AA")).isEqualTo(26);
    assertThat(ParseloSpec.compute("AZ")).isEqualTo(51);
    assertThat(ParseloSpec.compute("SZ")).isEqualTo(519);
    assertThat(ParseloSpec.compute("CUZ")).isEqualTo(2_599);
    assertThat(ParseloSpec.compute("NTQ")).isEqualTo(10_000);
  }

}