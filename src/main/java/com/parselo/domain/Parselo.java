package com.parselo.domain;

import static com.google.common.collect.ImmutableList.toImmutableList;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.StreamSupport;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;

import com.google.common.collect.Lists;
import com.google.common.io.Resources;

/**
 * Class for wrapping an HSSFWorkbook to provide utility methods for parsing arrays, matrices or annotated java objects.
 */
public class Parselo {

  private static final DataFormatter FORMATTER = new DataFormatter();
  private static final ParseloAnnotationParser ANNOTATION_PARSER = new ParseloAnnotationParser();

  private final HSSFWorkbook workbook;

  private Parselo(HSSFWorkbook workbook) {
    this.workbook = workbook;
  }

  /**
   * Create a new instance of Parselo given an xls workbook.
   *
   * @param workbook the xls workbook
   * @return a new instance of parselo
   */
  public static Parselo of(HSSFWorkbook workbook) {
    return new Parselo(workbook);
  }

  /**
   * Create a new instance of the Parselo given a filename for an xls.
   *
   * @param filename the name of the xls to wrap
   * @return the parselo instance
   */
  public static Parselo of(String filename) {
    URL url = Resources.getResource(filename);
    try (InputStream in = Resources.asByteSource(url).openStream()) {
      return Parselo.of(new HSSFWorkbook(in));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  //--------------------------------------------------------------------
  /**
   * Provide all the sheet names from the workbook.
   *
   * @return the list of sheet names
   */
  public List<String> getSheetNames() {
    return StreamSupport.stream(workbook.spliterator(), false)
        .map(Sheet::getSheetName)
        .collect(toImmutableList());
  }

  /**
   * Parse a string array defined by the spec from the given sheet.
   *
   * @param sheetName the sheet name
   * @param spec the specification for the array area
   * @return the array of strings
   * @throws IllegalArgumentException if the sheet name doesn't exist or the spec does not define an array area
   */
  public List<String> parseStringArray(String sheetName, ParseloSpec spec) {
    HSSFSheet sheet = getSheet(sheetName);
    if (spec == null) {
      throw new IllegalArgumentException("Spec cannot be null.");
    }

    if (spec.isHorizontalArray() || spec.isVerticalArray()) {
      return parseArray(sheet, spec);
    } else {
      throw new IllegalArgumentException(String.format(
          "Spec does not define an array. Either the start & end row must be the same or the " +
              "start cell and end cell must be the same. Instead found: %s", spec));
    }
  }

  /**
   * Parse a string matrix described by the specification from the given sheet name. The matrix will parse empty/null
   * cells as empty strings.
   *
   * @param sheetName the sheet name to parse
   * @param spec the matrix specification
   * @return the string matrix
   * @throws IllegalArgumentException if the sheet is null or the spec is null
   */
  public ParseloMatrix<String> parseStringMatrix(String sheetName, ParseloSpec spec) {
    HSSFSheet sheet = getSheet(sheetName);
    if (spec == null) {
      throw new IllegalArgumentException("Spec cannot be null.");
    }

    final int rowStart = spec.getRowStart();
    final int columnStart = spec.getColumnStartIndex();

    BiFunction<Integer, Integer, String> valueFunction =
        (rowOffset, columnOffset) -> FORMATTER.formatCellValue(sheet
                .getRow(rowStart + rowOffset)
                .getCell(columnStart + columnOffset));

    return ParseloMatrix.of(spec.rows(), spec.columns(), valueFunction);
  }

  /**
   * Parse a list of objects of a specific type from the sheet given. The type of the objects parsed needs to be
   * Parselo annotated.
   *
   * @param sheetName the sheet name to parse
   * @param clazz the class with type T
   * @param <T> the type of objects to parse
   * @return the list of objects of type T parsed from the sheet
   * @throws IllegalArgumentException if the class T is not annotated for Parselo
   */
  public <T> List<T> parse(String sheetName, Class<T> clazz) {
    HSSFSheet sheet = getSheet(sheetName);
    return ANNOTATION_PARSER.parse(sheet, clazz);
  }

  //--------------------------------------------------------------------
  private HSSFSheet getSheet(String sheetName) {
    if (sheetName == null) {
      throw new NullPointerException("SheetName cannot be null");
    }
    HSSFSheet sheet = workbook.getSheet(sheetName);
    if (sheet == null) {
      throw new IllegalArgumentException("No sheet found for name: " + sheetName);
    }
    return sheet;
  }

  private List<String> parseArray(HSSFSheet sheet, ParseloSpec spec) {
    List<String> array = Lists.newLinkedList();
    int rowStart = spec.getRowStart();
    int columnStart = spec.getColumnStartIndex();

    for (int rowOffset = 0; rowOffset < spec.rows(); rowOffset++) {
      for (int colOffset = 0; colOffset < spec.columns(); colOffset++) {
        HSSFCell cell = sheet.getRow(rowStart + rowOffset).getCell(columnStart + colOffset);
        array.add(FORMATTER.formatCellValue(cell));
      }
    }

    return array;
  }

}
