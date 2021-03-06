package com.parselo.domain;

import org.joda.beans.ImmutableBean;
import org.joda.beans.gen.BeanDefinition;
import org.joda.beans.gen.ImmutableValidator;
import org.joda.beans.gen.PropertyDefinition;

import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;

import org.joda.beans.Bean;
import org.joda.beans.JodaBeanUtils;
import org.joda.beans.MetaBean;
import org.joda.beans.MetaProperty;
import org.joda.beans.impl.direct.DirectFieldsBeanBuilder;
import org.joda.beans.impl.direct.DirectMetaBean;
import org.joda.beans.impl.direct.DirectMetaProperty;
import org.joda.beans.impl.direct.DirectMetaPropertyMap;

/**
 * Specification of an area to be parsed. Used by {@link Parselo}.
 * <p>
 * The specification will validate whether the rows are positive integers, if the columns are valid xls column letters
 * and whether the start/end bounds make sense.
 */
@BeanDefinition
public final class ParseloSpec implements ImmutableBean {

  /**
   * The start row index.
   */
  @PropertyDefinition
  private final int rowStart;
  /**
   * The end row index.
   */
  @PropertyDefinition
  private final int rowEnd;
  /**
   * The column start letters as present in the xls (case insensitive). Eg: 'D', 'AA', 'BCA'
   */
  @PropertyDefinition(validate = "notBlank", get = "private")
  private final String columnStart;
  /**
   * The column end letters as present in the xls (case insensitive). Eg: 'D', 'AA', 'BCA'
   */
  @PropertyDefinition(validate = "notBlank", get = "private")
  private final String columnEnd;

  /**
   * Check whether the specification represents an horizontal array, i.e. only one row.
   *
   * @return true if it does, false otherwise
   */
  public boolean isHorizontalArray() {
    return rows() == 1;
  }

  /**
   * Check whether the specification represents a vertical array, i.e only one column.
   *
   * @return true if it does, false otherwise
   */
  public boolean isVerticalArray() {
    return columns() == 1;
  }

  /**
   * The number of rows defined by this specification.
   *
   * @return the number of rows defined by this specification
   */
  public int rows() {
    return rowEnd - rowStart + 1;
  }

  /**
   * The number of columns defined by this specification.
   *
   * @return the number of columns defined by this specification.
   */
  public int columns() {
    return getColumnEndIndex() - getColumnStartIndex() + 1;
  }

  /**
   * Provide the index for the start cell.
   *
   * @return the zero-based index of the start cell
   */
  public int getColumnStartIndex() {
    return ExcelUtils.columnIndex(columnStart);
  }

  /**
   * Get the index of the end cell.
   *
   * @return the zero-based index of the end cell
   */
  public int getColumnEndIndex() {
    return ExcelUtils.columnIndex(columnEnd);
  }

  //------------------------------------------------------------------
  @ImmutableValidator
  private void validate() {
    ExcelUtils.isValidRowNumber(rowStart, "rowStart");
    ExcelUtils.isValidRowNumber(rowEnd, "rowEnd");
    ExcelUtils.isExcelColumn(columnStart, "columnStart");
    ExcelUtils.isExcelColumn(columnEnd, "columnEnd");
    if (rowStart > rowEnd) {
      throw new IllegalArgumentException(String.format(
          "rowStart[%d] cannot be after rowEnd[%d]",
          rowStart,
          rowEnd));
    }
    if (columnStart.compareToIgnoreCase(columnEnd) > 0) {
      throw new IllegalArgumentException(String.format(
          "columnStart[%s] cannot be after columnEnd[%s]",
          columnStart.toUpperCase(Locale.ENGLISH),
          columnEnd.toUpperCase(Locale.ENGLISH)));
    }
  }

  //------------------------- AUTOGENERATED START -------------------------
  /**
   * The meta-bean for {@code ParseloSpec}.
   * @return the meta-bean, not null
   */
  public static ParseloSpec.Meta meta() {
    return ParseloSpec.Meta.INSTANCE;
  }

  static {
    MetaBean.register(ParseloSpec.Meta.INSTANCE);
  }

  /**
   * Returns a builder used to create an instance of the bean.
   * @return the builder, not null
   */
  public static ParseloSpec.Builder builder() {
    return new ParseloSpec.Builder();
  }

  private ParseloSpec(
      int rowStart,
      int rowEnd,
      String columnStart,
      String columnEnd) {
    JodaBeanUtils.notBlank(columnStart, "columnStart");
    JodaBeanUtils.notBlank(columnEnd, "columnEnd");
    this.rowStart = rowStart;
    this.rowEnd = rowEnd;
    this.columnStart = columnStart;
    this.columnEnd = columnEnd;
    validate();
  }

  @Override
  public ParseloSpec.Meta metaBean() {
    return ParseloSpec.Meta.INSTANCE;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the start row (zero-based) index.
   * @return the value of the property
   */
  public int getRowStart() {
    return rowStart;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the end row (zero-based) index.
   * @return the value of the property
   */
  public int getRowEnd() {
    return rowEnd;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the column start letters as present in the xls (case insensitive). Eg: 'D', 'AA', 'BCA'
   * @return the value of the property, not blank
   */
  private String getColumnStart() {
    return columnStart;
  }

  //-----------------------------------------------------------------------
  /**
   * Gets the column end letters as present in the xls (case insensitive). Eg: 'D', 'AA', 'BCA'
   * @return the value of the property, not blank
   */
  private String getColumnEnd() {
    return columnEnd;
  }

  //-----------------------------------------------------------------------
  /**
   * Returns a builder that allows this bean to be mutated.
   * @return the mutable builder, not null
   */
  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }
    if (obj != null && obj.getClass() == this.getClass()) {
      ParseloSpec other = (ParseloSpec) obj;
      return (rowStart == other.rowStart) &&
          (rowEnd == other.rowEnd) &&
          JodaBeanUtils.equal(columnStart, other.columnStart) &&
          JodaBeanUtils.equal(columnEnd, other.columnEnd);
    }
    return false;
  }

  @Override
  public int hashCode() {
    int hash = getClass().hashCode();
    hash = hash * 31 + JodaBeanUtils.hashCode(rowStart);
    hash = hash * 31 + JodaBeanUtils.hashCode(rowEnd);
    hash = hash * 31 + JodaBeanUtils.hashCode(columnStart);
    hash = hash * 31 + JodaBeanUtils.hashCode(columnEnd);
    return hash;
  }

  @Override
  public String toString() {
    StringBuilder buf = new StringBuilder(160);
    buf.append("ParseloSpec{");
    buf.append("rowStart").append('=').append(rowStart).append(',').append(' ');
    buf.append("rowEnd").append('=').append(rowEnd).append(',').append(' ');
    buf.append("columnStart").append('=').append(columnStart).append(',').append(' ');
    buf.append("columnEnd").append('=').append(JodaBeanUtils.toString(columnEnd));
    buf.append('}');
    return buf.toString();
  }

  //-----------------------------------------------------------------------
  /**
   * The meta-bean for {@code ParseloSpec}.
   */
  public static final class Meta extends DirectMetaBean {
    /**
     * The singleton instance of the meta-bean.
     */
    static final Meta INSTANCE = new Meta();

    /**
     * The meta-property for the {@code rowStart} property.
     */
    private final MetaProperty<Integer> rowStart = DirectMetaProperty.ofImmutable(
        this, "rowStart", ParseloSpec.class, Integer.TYPE);
    /**
     * The meta-property for the {@code rowEnd} property.
     */
    private final MetaProperty<Integer> rowEnd = DirectMetaProperty.ofImmutable(
        this, "rowEnd", ParseloSpec.class, Integer.TYPE);
    /**
     * The meta-property for the {@code columnStart} property.
     */
    private final MetaProperty<String> columnStart = DirectMetaProperty.ofImmutable(
        this, "columnStart", ParseloSpec.class, String.class);
    /**
     * The meta-property for the {@code columnEnd} property.
     */
    private final MetaProperty<String> columnEnd = DirectMetaProperty.ofImmutable(
        this, "columnEnd", ParseloSpec.class, String.class);
    /**
     * The meta-properties.
     */
    private final Map<String, MetaProperty<?>> metaPropertyMap$ = new DirectMetaPropertyMap(
        this, null,
        "rowStart",
        "rowEnd",
        "columnStart",
        "columnEnd");

    /**
     * Restricted constructor.
     */
    private Meta() {
    }

    @Override
    protected MetaProperty<?> metaPropertyGet(String propertyName) {
      switch (propertyName.hashCode()) {
        case 32649896:  // rowStart
          return rowStart;
        case -925118303:  // rowEnd
          return rowEnd;
        case -845830484:  // columnStart
          return columnStart;
        case -2146142811:  // columnEnd
          return columnEnd;
      }
      return super.metaPropertyGet(propertyName);
    }

    @Override
    public ParseloSpec.Builder builder() {
      return new ParseloSpec.Builder();
    }

    @Override
    public Class<? extends ParseloSpec> beanType() {
      return ParseloSpec.class;
    }

    @Override
    public Map<String, MetaProperty<?>> metaPropertyMap() {
      return metaPropertyMap$;
    }

    //-----------------------------------------------------------------------
    /**
     * The meta-property for the {@code rowStart} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Integer> rowStart() {
      return rowStart;
    }

    /**
     * The meta-property for the {@code rowEnd} property.
     * @return the meta-property, not null
     */
    public MetaProperty<Integer> rowEnd() {
      return rowEnd;
    }

    /**
     * The meta-property for the {@code columnStart} property.
     * @return the meta-property, not null
     */
    public MetaProperty<String> columnStart() {
      return columnStart;
    }

    /**
     * The meta-property for the {@code columnEnd} property.
     * @return the meta-property, not null
     */
    public MetaProperty<String> columnEnd() {
      return columnEnd;
    }

    //-----------------------------------------------------------------------
    @Override
    protected Object propertyGet(Bean bean, String propertyName, boolean quiet) {
      switch (propertyName.hashCode()) {
        case 32649896:  // rowStart
          return ((ParseloSpec) bean).getRowStart();
        case -925118303:  // rowEnd
          return ((ParseloSpec) bean).getRowEnd();
        case -845830484:  // columnStart
          return ((ParseloSpec) bean).getColumnStart();
        case -2146142811:  // columnEnd
          return ((ParseloSpec) bean).getColumnEnd();
      }
      return super.propertyGet(bean, propertyName, quiet);
    }

    @Override
    protected void propertySet(Bean bean, String propertyName, Object newValue, boolean quiet) {
      metaProperty(propertyName);
      if (quiet) {
        return;
      }
      throw new UnsupportedOperationException("Property cannot be written: " + propertyName);
    }

  }

  //-----------------------------------------------------------------------
  /**
   * The bean-builder for {@code ParseloSpec}.
   */
  public static final class Builder extends DirectFieldsBeanBuilder<ParseloSpec> {

    private int rowStart;
    private int rowEnd;
    private String columnStart;
    private String columnEnd;

    /**
     * Restricted constructor.
     */
    private Builder() {
    }

    /**
     * Restricted copy constructor.
     * @param beanToCopy  the bean to copy from, not null
     */
    private Builder(ParseloSpec beanToCopy) {
      this.rowStart = beanToCopy.getRowStart();
      this.rowEnd = beanToCopy.getRowEnd();
      this.columnStart = beanToCopy.getColumnStart();
      this.columnEnd = beanToCopy.getColumnEnd();
    }

    //-----------------------------------------------------------------------
    @Override
    public Object get(String propertyName) {
      switch (propertyName.hashCode()) {
        case 32649896:  // rowStart
          return rowStart;
        case -925118303:  // rowEnd
          return rowEnd;
        case -845830484:  // columnStart
          return columnStart;
        case -2146142811:  // columnEnd
          return columnEnd;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
    }

    @Override
    public Builder set(String propertyName, Object newValue) {
      switch (propertyName.hashCode()) {
        case 32649896:  // rowStart
          this.rowStart = (Integer) newValue;
          break;
        case -925118303:  // rowEnd
          this.rowEnd = (Integer) newValue;
          break;
        case -845830484:  // columnStart
          this.columnStart = (String) newValue;
          break;
        case -2146142811:  // columnEnd
          this.columnEnd = (String) newValue;
          break;
        default:
          throw new NoSuchElementException("Unknown property: " + propertyName);
      }
      return this;
    }

    @Override
    public Builder set(MetaProperty<?> property, Object value) {
      super.set(property, value);
      return this;
    }

    @Override
    public ParseloSpec build() {
      return new ParseloSpec(
          rowStart,
          rowEnd,
          columnStart,
          columnEnd);
    }

    //-----------------------------------------------------------------------
    /**
     * Sets the start row (zero-based) index.
     * @param rowStart  the new value
     * @return this, for chaining, not null
     */
    public Builder rowStart(int rowStart) {
      this.rowStart = rowStart;
      return this;
    }

    /**
     * Sets the end row (zero-based) index.
     * @param rowEnd  the new value
     * @return this, for chaining, not null
     */
    public Builder rowEnd(int rowEnd) {
      this.rowEnd = rowEnd;
      return this;
    }

    /**
     * Sets the column start letters as present in the xls (case insensitive). Eg: 'D', 'AA', 'BCA'
     * @param columnStart  the new value, not blank
     * @return this, for chaining, not null
     */
    public Builder columnStart(String columnStart) {
      JodaBeanUtils.notBlank(columnStart, "columnStart");
      this.columnStart = columnStart;
      return this;
    }

    /**
     * Sets the column end letters as present in the xls (case insensitive). Eg: 'D', 'AA', 'BCA'
     * @param columnEnd  the new value, not blank
     * @return this, for chaining, not null
     */
    public Builder columnEnd(String columnEnd) {
      JodaBeanUtils.notBlank(columnEnd, "columnEnd");
      this.columnEnd = columnEnd;
      return this;
    }

    //-----------------------------------------------------------------------
    @Override
    public String toString() {
      StringBuilder buf = new StringBuilder(160);
      buf.append("ParseloSpec.Builder{");
      buf.append("rowStart").append('=').append(JodaBeanUtils.toString(rowStart)).append(',').append(' ');
      buf.append("rowEnd").append('=').append(JodaBeanUtils.toString(rowEnd)).append(',').append(' ');
      buf.append("columnStart").append('=').append(JodaBeanUtils.toString(columnStart)).append(',').append(' ');
      buf.append("columnEnd").append('=').append(JodaBeanUtils.toString(columnEnd));
      buf.append('}');
      return buf.toString();
    }

  }

  //-------------------------- AUTOGENERATED END --------------------------
}
