package com.parselo.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.parselo.domain.ConfigurationType;

/**
 * This annotation is used to mark classes as mappable from excel rows using Parselo.
 *
 * <p><strong>Note 1:</strong> For {@link ConfigurationType#STATIC} it is expected that you provide the 'start' and 'end' parameters
 * on this annotation. This will tell Parselo where to start and end parsing rows. The class fields will need to be
 * annotated with the corresponding {@link ParseloColumn} annotations to tell Parselo the expected columns to parse.
 *
 * <p><strong>Note 2:</strong> For {@link ConfigurationType#DYNAMIC}, the class fields will need to be annotated with
 * {@link ParseloPosition} which will tell Parselo the position of the field in the defined area to parse. (i.e.
 * the column offset from the first position)
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ParseloRow {

  /**
   * The type of configuration that should be expected by Parselo as defined by {@link ConfigurationType}.
   *
   * @return the type of configuration that Parselo will expect.
   */
  ConfigurationType type() default ConfigurationType.DYNAMIC;

  /**
   * The Excel row index where the first row to parse is. This argument is ignored for
   * {@link ConfigurationType#DYNAMIC}.
   * <p>Note: The index is one-based (i.e. minimum possible row is 1)
   *
   * @return the one-based index of the first row
   */
  int start() default 0;

  /**
   * The Excel row index where the last row to parse is. This argument is ignored for {@link ConfigurationType#DYNAMIC}.
   * <p>Note: The index is one-based (i.e. minimum possible row is 1)
   *
   * @return the one-based index of the last row
   */
  int end() default 0;
}
