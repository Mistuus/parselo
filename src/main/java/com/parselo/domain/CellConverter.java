package com.parselo.domain;

import org.apache.poi.hssf.usermodel.HSSFCell;

public interface CellConverter<T> {

  T convert(HSSFCell cell);

}

