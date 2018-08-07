package com.parselo.domain;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.ParsePosition;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.parselo.annotations.Parselo;
import com.parselo.annotations.ParseloColumn;
import com.parselo.annotations.ParseloPosition;
import com.parselo.annotations.ParseloRow;
import com.parselo.exception.InvalidConfigurationException;

/**
 * Class for extracting Parselo annotated objects from a given sheet.
 */
class ParseloAnnotationParser {

  /**
   * Parse a given sheet for a list of objects of the provided type.
   *
   * @param sheet the sheet to parseStatic
   * @param clazz the class of type T
   * @param <T> the type of the resulting parsed objects
   * @return the list of parsed objects of type T from the sheet
   * @throws IllegalArgumentException if no ParseloRow/ParseloColumn annotations are found or a particular field type
   * cannot be parsed from the sheet
   */
  <T> List<T> parseStatic(HSSFSheet sheet, Class<T> clazz) {
    try {
      ParseloRow rowAnnotation = extractClassAnnotation(clazz, ParseloRow.class);
      List<Field> fields = extractSortedColumnAnnotatedFields(clazz);
      ParseloSpec spec = createSpec(rowAnnotation, extractColumnAnnotations(fields));
      return parseRows(sheet, spec, fields, clazz.getConstructor());
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(
          String.format(
              "Class %s must have a public non-args constructor",
              clazz.getName()));
    }
  }

  /**
   * Parse a given sheet for a list of objects of the provided type. The area parsed will be driven by
   * the specification provided.
   *
   * @param sheet the sheet to parseStatic
   * @param clazz the class of type T
   * @param <T> the type of the resulting parsed objects
   * @return the list of parsed objects of type T from the sheet
   * @throws IllegalArgumentException if no ParseloRow/ParseloColumn annotations are found or a particular field type
   * cannot be parsed from the sheet
   */
  <T> List<T> parseDynamic(HSSFSheet sheet, Class<T> clazz, ParseloSpec spec) {
    try {
      validateClassIsAnnotated(clazz, Parselo.class);
      List<Field> fields = extractSortedPositionAnnotatedFields(clazz);
      validateFieldsAndSpec(fields, spec);
      return parseRows(sheet, spec, fields, clazz.getConstructor());
    } catch (NoSuchMethodException e) {
      throw new RuntimeException(
          String.format(
              "Class %s must have a public non-args constructor",
              clazz.getName()));
    }
  }

  private void validateFieldsAndSpec(List<Field> fields, ParseloSpec spec) {
    if (fields.size() != spec.columns()) {
      throw new InvalidConfigurationException(String.format(
          "Expecting nr. of annotated fields=%d to equal nr. columns from spec=%d",
          fields.size(),
          spec.columns()));
    }
  }

  // The fields must be sorted in ascending order of their annotations's parameters (so by column name for
  // ParseloColumns or by position for the ParseloPosition)
  private <T> List<T> parseRows(
      HSSFSheet sheet,
      ParseloSpec spec,
      List<Field> fields,
      Constructor<T> clazzConstructor) {

    try {
      validateBounds(spec, sheet);

      int rowStart = spec.getRowStart() - 1;
      int rowCount = spec.rows();
      int columnStart = spec.getColumnStartIndex() - 1;
      int columnCount = spec.columns();
      clazzConstructor.setAccessible(true);


      List<T> rows = Lists.newLinkedList();
      for (int rowOffset = 0; rowOffset < rowCount; rowOffset++) {
        T parsedObj = clazzConstructor.newInstance();

        for (int columnOffset = 0; columnOffset < columnCount; columnOffset++) {
          HSSFCell cell = sheet.getRow(rowStart + rowOffset).getCell(columnStart + columnOffset);
          Field targetField = fields.get(columnOffset);
          Object parsed = convertCell(cell, targetField.getType());
          targetField.setAccessible(true);
          targetField.set(parsedObj, parsed);
        }

        rows.add(parsedObj);
      }
      return rows;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void validateBounds(ParseloSpec spec, HSSFSheet sheet) {
    if (spec.getRowStart() - 1 < sheet.getFirstRowNum() || spec.getRowEnd() - 1 > sheet.getLastRowNum()) {
      throw new InvalidConfigurationException(String.format(
          "Spec rows must be within the bounds of the sheet %s. Accepted bounds (one-based index): [%d, %d]",
          sheet.getSheetName(),
          sheet.getFirstRowNum() + 1,
          sheet.getLastRowNum() + 1));
    }
  }

  private Object convertCell(HSSFCell cell, Class<?> conversionType) {
    try {
      if (cell == null) {
        return null;
      } else {
        return CellConverters.getConverter(conversionType).convert(cell);
      }
    } catch (Exception e) {
      throw new RuntimeException(String.format(
          "Exception encountered for cell at row=%d (zero-based index) and column=%d (zero-based index) " +
              "when trying to convert to type=%s",
          cell.getRowIndex(),
          cell.getColumnIndex(),
          conversionType.getName()));
    }
  }

  private List<Field> extractSortedPositionAnnotatedFields(Class<?> clazz) {
    ImmutableList<Field> fields = Arrays.stream(clazz.getDeclaredFields())
        .filter(field -> field.getAnnotation(ParseloPosition.class) != null)
        .sorted(Comparator.comparing(field -> field.getAnnotation(ParseloPosition.class).position()))
        .collect(ImmutableList.toImmutableList());

    if (fields.isEmpty()) {
      throw new InvalidConfigurationException(String.format(
          "Class %s has no fields annotated with %s.",
          clazz.getName(),
          ParsePosition.class));
    }

    return fields;
  }

  private ParseloSpec createSpec(ParseloRow rowAnnotation, List<ParseloColumn> columnAnnotations) {
    ImmutableList<String> columnNames = columnAnnotations.stream()
        .map(ParseloColumn::name)
        .sorted()
        .collect(ImmutableList.toImmutableList());

    return ParseloSpec.builder()
        .rowStart(rowAnnotation.start())
        .rowEnd(rowAnnotation.end())
        .columnStart(columnNames.get(0))
        .columnEnd(columnNames.get(columnNames.size() - 1))
        .build();
  }

  private ImmutableList<ParseloColumn> extractColumnAnnotations(List<Field> fields) {
    return fields.stream()
        .map(field -> field.getAnnotation(ParseloColumn.class))
        .collect(ImmutableList.toImmutableList());
  }

  private List<Field> extractSortedColumnAnnotatedFields(Class<?> clazz) {
    ImmutableList<Field> fields = Arrays.stream(clazz.getDeclaredFields())
        .filter(field -> field.getAnnotation(ParseloColumn.class) != null)
        .sorted(Comparator.comparing(field -> field.getAnnotation(ParseloColumn.class).name()))
        .collect(ImmutableList.toImmutableList());

    if (fields.isEmpty()) {
      throw new InvalidConfigurationException(String.format(
          "Class %s has no fields annotated with %s",
          clazz.getName(),
          ParseloColumn.class.getName()));
    }

    return fields;
  }

  private <T extends Annotation> void validateClassIsAnnotated(Class<?> clazz, Class<T> annotationClass) {
    if (clazz.getAnnotation(annotationClass) == null) {
      throw new InvalidConfigurationException(String.format(
          "Expecting class %s to be annotated with %s",
          clazz.getName(),
          annotationClass.getName()));
    }
  }

  private <T extends Annotation> T extractClassAnnotation(Class<?> clazz, Class<T> annotationClass) {
    return Optional.ofNullable(clazz.getAnnotation(annotationClass))
        .orElseThrow(() ->
            new InvalidConfigurationException(String.format(
                "Class %s needs to be annotated with %s",
                clazz.getCanonicalName(),
                annotationClass.getName())));
  }
}
