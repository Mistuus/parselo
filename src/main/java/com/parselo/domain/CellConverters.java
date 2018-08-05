package com.parselo.domain;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.DataFormatter;

public class CellConverters {

  public static final CellConverter<String> TO_STRING = new CellConverter<String>() {
    private final DataFormatter DATA_FORMATTER = new DataFormatter();

    @Override
    public String convert(HSSFCell cell) {
      return DATA_FORMATTER.formatCellValue(cell);
    }
  };

  public static final CellConverter<Double> TO_DOUBLE = HSSFCell::getNumericCellValue;
}
