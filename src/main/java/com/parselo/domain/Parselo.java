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
import org.apache.poi.ss.usermodel.Sheet;
import org.joda.beans.JodaBeanUtils;

import com.google.common.collect.Lists;
import com.google.common.io.Resources;

/**
 * Class for wrapping an HSSFWorkbook to provide utility methods for parsing arrays, matrices or annotated java objects.
 */
public class Parselo {

  private final ParseloAnnotationParser annotationParser;

  private final HSSFWorkbook workbook;

  private Parselo(HSSFWorkbook workbook, ParseloAnnotationParser annotationParser) {
    this.workbook = workbook;
    this.annotationParser = annotationParser;
  }

  /**
   * Create a new instance of Parselo given an xls workbook.
   *
   * @param workbook the xls workbook
   * @return a new instance of parselo
   */
  public static Parselo of(HSSFWorkbook workbook) {
    return new Parselo(workbook, new ParseloAnnotationParser());
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
   * Parse an array defined by the spec from the given sheet. Empty/Null cells will be mapped to the default value
   * specified in {@link CellConverter#getDefault()}.
   *
   * @param sheetName the sheet name
   * @param cellConverter the function to convert a cell to an object of type T
   * @param spec the specification for the array area
   * @param <T> the type of the elements in the list
   * @return the list of elements
   * @throws IllegalArgumentException if the sheet name doesn't exist or the spec does not define an array area
   */
  public <T> List<T> parseArray(String sheetName, CellConverter<T> cellConverter, ParseloSpec spec) {
    JodaBeanUtils.notNull(spec, "spec");
    JodaBeanUtils.notNull(cellConverter, "cellConverter");
    HSSFSheet sheet = getSheet(sheetName);

    if (spec.isHorizontalArray() || spec.isVerticalArray()) {
      return parseArray(sheet, spec, cellConverter);
    } else {
      throw new IllegalArgumentException(String.format(
          "Spec does not define an array. Either the start & end row must be the same or the " +
              "start cell and end cell must be the same. Instead found: %s", spec));
    }
  }

  /**
   * Parse a matrix described by the specification from the given sheet name. Empty/Null cells will be mapped to the
   * default value specified in {@link CellConverter#getDefault()}.
   *
   * @param sheetName the sheet name to parse
   * @param cellConverter the function to convert a cell to an object of type T
   * @param spec the matrix specification
   * @param <T> the type of the elements in the matrix
   * @return the matrix
   * @throws IllegalArgumentException if the sheet is null or the spec is null
   */
  public <T> ParseloMatrix<T> parseMatrix(
      String sheetName,
      CellConverter<T> cellConverter,
      ParseloSpec spec) {

    JodaBeanUtils.notNull(spec, "spec");
    JodaBeanUtils.notNull(cellConverter, "cellConverter");
    HSSFSheet sheet = getSheet(sheetName);
    return parseMatrix(sheet, spec, cellConverter);
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
    return annotationParser.parse(sheet, clazz);
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

  private <T> List<T> parseArray(HSSFSheet sheet, ParseloSpec spec, CellConverter<T> cellConverter) {
    List<T> array = Lists.newLinkedList();
    int rowStart = spec.getRowStart();
    int columnStart = spec.getColumnStartIndex();

    for (int rowOffset = 0; rowOffset < spec.rows(); rowOffset++) {
      for (int colOffset = 0; colOffset < spec.columns(); colOffset++) {
        HSSFCell cell = sheet.getRow(rowStart + rowOffset).getCell(columnStart + colOffset);
        array.add(cellConverter.convertWithDefault(cell));
      }
    }

    return array;
  }

  private <T> ParseloMatrix<T> parseMatrix(HSSFSheet sheet, ParseloSpec spec, CellConverter<T> cellConverter) {
    final int rowStart = spec.getRowStart();
    final int columnStart = spec.getColumnStartIndex();

    BiFunction<Integer, Integer, T> valueFunction =
        (rowOffset, columnOffset) -> cellConverter.convertWithDefault(
            sheet.getRow(rowStart + rowOffset)
                .getCell(columnStart + columnOffset));

    return ParseloMatrix.of(spec.rows(), spec.columns(), valueFunction);
  }
}
