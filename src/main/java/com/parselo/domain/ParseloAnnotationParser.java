package com.parselo.domain;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.parselo.annotations.ParseloRow;
import com.parselo.annotations.ParseloColumn;

/**
 * Class for extracting Parselo annotated objects from a given sheet.
 */
class ParseloAnnotationParser {

  /**
   * Parse a given sheet for a list of objects of the provided type.
   *
   * @param sheet the sheet to parse
   * @param clazz the class of type T
   * @param <T> the type of the resulting parsed objects
   * @return the list of parsed objects of type T from the sheet
   * @throws IllegalArgumentException if no ParseloRow annotation is found or a particular field type cannot be parsed
   * from the sheet
   */
  <T> List<T> parse(HSSFSheet sheet, Class<T> clazz) {
    try {
      ParseloRow annotation = extractAnnotation(clazz);
      int rowStart = annotation.start();
      int rowEnd = annotation.end();
      int rowCount = rowEnd - rowStart + 1;

      Map<Integer, Field> colIdxToField = extractFieldPositions(clazz);
      Set<Integer> columnPositions = colIdxToField.keySet();

      List<T> rows = Lists.newLinkedList();

      for (int rowOffset = 0; rowOffset < rowCount; rowOffset++) {
        Constructor<T> constructor = clazz.getConstructor();
        T parsedObj = constructor.newInstance();

        for (Integer columnIdx : columnPositions) {
          HSSFCell cell = sheet.getRow(rowStart + rowOffset - 1).getCell(columnIdx);

          Field field = colIdxToField.get(columnIdx);
          field.setAccessible(true);
          field.set(parsedObj, ExcelUtils.extractValue(cell, field));
        }

        rows.add(parsedObj);
      }

      return rows;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private <T> Map<Integer, Field> extractFieldPositions(Class<T> clazz) {
    Map<Integer, Field> positionToField = Maps.newHashMap();

    for (Field field : clazz.getDeclaredFields()) {
      ParseloColumn columnAnnotation = field.getAnnotation(ParseloColumn.class);

      if (columnAnnotation != null) {
        int cellIndex = ExcelUtils.columnIndex(columnAnnotation.name());
        // TODO: what if we have duplicate cell indexes? Do we error or override?
        positionToField.put(cellIndex, field);
      }
    }

    return positionToField;
  }

  private <T> ParseloRow extractAnnotation(Class<T> clazz) {
    ParseloRow annotation = clazz.getAnnotation(ParseloRow.class);
    if (annotation == null) {
      throw new IllegalArgumentException(String.format(
          "Class %s needs to be annotated with %s",
          clazz.getCanonicalName(),
          ParseloRow.class.getName()));
    }
    return annotation;
  }
}
