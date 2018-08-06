package com.parselo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ExcelUtilsTest {

  @Test
  void singleLetterColumn() {
    assertThat(ExcelUtils.columnIndex("AA")).isEqualTo(27);
    assertThat(ExcelUtils.columnIndex("AZ")).isEqualTo(52);
    assertThat(ExcelUtils.columnIndex("SZ")).isEqualTo(520);
    assertThat(ExcelUtils.columnIndex("CUZ")).isEqualTo(2_600);
    assertThat(ExcelUtils.columnIndex("NTP")).isEqualTo(10_000);
  }

}