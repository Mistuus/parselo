package com.parselo.domain;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.joda.beans.JodaBeanUtils;

public class CellConverters {


  public static final CellConverter<String> TO_STRING = new CellConverter<String>() {
    private static final String EMPTY_STRING = "";
    private final DataFormatter DATA_FORMATTER = new DataFormatter();

    @Override
    public String convert(HSSFCell cell) {
      return DATA_FORMATTER.formatCellValue(cell);
    }

    @Override
    public String getDefault() {
      return EMPTY_STRING;
    }
  };

  public static final CellConverter<Integer> TO_INTEGER = new CellConverter<Integer>() {
    @Override
    public Integer convert(HSSFCell cell) {
      return ((int) cell.getNumericCellValue());
    }

    @Override
    public Integer getDefault() {
      return Integer.MIN_VALUE;
    }
  };

  public static final CellConverter<Double> TO_DOUBLE = new CellConverter<Double>() {
    @Override
    public Double convert(HSSFCell cell) {
      return cell.getNumericCellValue();
    }

    @Override
    public Double getDefault() {
      return Double.NaN;
    }
  };

  public static final CellConverter<LocalDate> TO_LOCAL_DATE = new CellConverter<LocalDate>() {
    @Override
    public LocalDate convert(HSSFCell cell) {
      return cell.getDateCellValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    @Override
    public LocalDate getDefault() {
      return LocalDate.MIN;
    }
  };

  public static final Map<Class<?>, CellConverter<?>> conversionTargetToConverter = new HashMap<>();

  static {
    addToMap(conversionTargetToConverter, String.class, TO_STRING);
    addToMap(conversionTargetToConverter, Integer.class, TO_INTEGER);
    addToMap(conversionTargetToConverter, Double.class, TO_DOUBLE);
    addToMap(conversionTargetToConverter, LocalDate.class, TO_LOCAL_DATE);
  }

  static Optional<CellConverter<?>> findConverter(Class<?> conversionTarget) {
    JodaBeanUtils.notNull(conversionTarget, "fieldToPopulate");
    return Optional.ofNullable(conversionTargetToConverter.get(conversionTarget));
  }

  static CellConverter<?> getConverter(Class<?> conversionTarget) {
    JodaBeanUtils.notNull(conversionTarget, "fieldToPopulate");

    return findConverter(conversionTarget)
        .orElseThrow(() -> new IllegalArgumentException(String.format(
            "Parselo does not support conversion to type '%s'",
            conversionTarget.getName())));
  }

  // Type safe method for adding the right converters for the right types
  private static <T> void addToMap(
      Map<Class<?>, CellConverter<?>> typeToConverter,
      Class<T> type,
      CellConverter<T> cellConverter) {
    typeToConverter.put(type, cellConverter);
  }
}
