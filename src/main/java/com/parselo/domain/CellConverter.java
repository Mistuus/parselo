package com.parselo.domain;

import org.apache.poi.hssf.usermodel.HSSFCell;

public interface CellConverter<T> {

  T convert(HSSFCell cell);

  T getDefault();

  default T convertWithDefault(HSSFCell cell) {
    if (cell == null) {
      return getDefault();
    } else {
      T converted = convert(cell);
      return converted == null ? getDefault() : converted;
    }
  }
}

