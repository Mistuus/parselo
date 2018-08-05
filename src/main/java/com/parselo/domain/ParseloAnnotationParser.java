package com.parselo.domain;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

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
    ParseloRow annotation = extractAnnotation(clazz);
    Map<Integer, Field> colIdxToField = extractFieldPositions(clazz);
    return parseRows(sheet, annotation, colIdxToField, clazz);
  }

  private <T> List<T> parseRows(
      HSSFSheet sheet,
      ParseloRow annotation,
      Map<Integer, Field> positionToField,
      Class<T> clazz) {

    try {
      int rowStart = annotation.start();
      int rowCount = annotation.end() - rowStart + 1;
      Constructor<T> objectConstructor = clazz.getConstructor();
      objectConstructor.setAccessible(true);

      List<T> rows = Lists.newLinkedList();
      for (int rowOffset = 0; rowOffset < rowCount; rowOffset++) {
        T parsedObj = objectConstructor.newInstance();

        for (Integer columnIdx : positionToField.keySet()) {
          HSSFCell cell = sheet.getRow(rowStart + rowOffset - 1).getCell(columnIdx);
          Field targetField = positionToField.get(columnIdx);
          Object parsed = convertCell(cell, targetField.getType());
          targetField.setAccessible(true);
          targetField.set(parsedObj, parsed);
        }

        rows.add(parsedObj);
      }
      return rows;
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(
          String.format("Class %s must have a public non-args constructor",
          clazz.getName()));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private Object convertCell(HSSFCell cell, Class<?> conversionType) {
    if (cell == null) {
      return null;
    } else {
      return CellConverters.getConverter(conversionType).convert(cell);
    }
  }

  private <T> Map<Integer, Field> extractFieldPositions(Class<T> clazz) {
    Map<Integer, Field> positionToField = Maps.newHashMap();

    for (Field field : clazz.getDeclaredFields()) {
      ParseloColumn columnAnnotation = field.getAnnotation(ParseloColumn.class);

      if (columnAnnotation != null) {
        int cellIndex = ExcelUtils.columnIndex(columnAnnotation.name());
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
