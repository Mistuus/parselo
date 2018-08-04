package com.parselo.domain;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utilities class for dealing with different excel specific challenges.
 */
class ExcelUtils {

  private static final Logger logger = LoggerFactory.getLogger(ExcelUtils.class);
  private static final DataFormatter DATA_FORMATTER = new DataFormatter();

  /**
   * Convert a case insensitive column name (like 'A', 'bb', 'cdA', 'CzA' etc) to the zero-based index of that column.
   *
   * @param column the column name
   * @return the zero-based index of that column name
   * @throws IllegalArgumentException if the column name doesn't contain only alphabet letters
   */
  static int columnIndex(String column) {
    column = column.toUpperCase(Locale.ENGLISH);
    if (!column.matches("[A-Z]*")) {
      throw new IllegalArgumentException(String.format(
          "Expecting column to be only english alphabet letters. Found '%s'",
          column));
    }

    int position = 0;
    int length = column.length();
    for (int i = 0; i < length; i++) {
      int charIdx = column.charAt(i) - 'A';
      position += (charIdx + 1) * (int) Math.pow(26, length - i - 1);
    }
    return position - 1;
  }

  /**
   * Extract the object with the same type as the fieldToPopulate from the given cell. The method will return the
   * default value 'null' when a null cell is provided to the method.
   *
   * @param cell the cell to extract the data out from
   * @param fieldToPopulate the field we want to assign the extracted object to
   * @return the object extracted from the cell having the same type as the type of the fieldToPopulate
   * @throws IllegalArgumentException if a type is not supported or a cell cannot be converted to the required type.
   */
  static Object extractValue(HSSFCell cell, Field fieldToPopulate) {
    if (cell == null) {
      logger.debug("Cell to parse is null. Using default value 'null'");
      return null;
    }

    Class<?> cellType = fieldToPopulate.getType();

    if (String.class.equals(cellType)) {
      return DATA_FORMATTER.formatCellValue(cell);
    }

    if (Integer.class.equals(cellType)) {
      return (int) cell.getNumericCellValue();
    }

    if (Double.class.equals(cellType)) {
      return cell.getNumericCellValue();
    }

    if (LocalDate.class.equals(cellType)) {
      if (DateUtil.isCellDateFormatted(cell)) {
        Date input = DateUtil.getJavaDate(cell.getNumericCellValue());
        return input.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
      } else {
        throw new IllegalArgumentException(String.format(
            "%s does not contain a valid Excel Date. Found value: '%s'",
            toDescription(cell),
            DATA_FORMATTER.formatCellValue(cell)));
      }
    }

    throw new IllegalArgumentException(String.format(
        "Parselo does not support conversion to type '%s' for field name '%s'.",
        cellType.getName(),
        fieldToPopulate.getName()));
  }

  private static String toDescription(HSSFCell cell) {
    return "Cell: {" +
        "row: " + cell.getRowIndex() + ", " +
        "column: " + cell.getColumnIndex() +
        "}";
  }
}
