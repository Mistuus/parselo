package com.parselo.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ExcelUtilsTest {

  @Test
  void singleLetterColumn() {
    assertThat(ExcelUtils.columnIndex("AA")).isEqualTo(26);
    assertThat(ExcelUtils.columnIndex("AZ")).isEqualTo(51);
    assertThat(ExcelUtils.columnIndex("SZ")).isEqualTo(519);
    assertThat(ExcelUtils.columnIndex("CUZ")).isEqualTo(2_599);
    assertThat(ExcelUtils.columnIndex("NTQ")).isEqualTo(10_000);
  }

}