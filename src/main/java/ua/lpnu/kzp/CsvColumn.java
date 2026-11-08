package ua.lpnu.kzp;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field that should be exported as a CSV column.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CsvColumn {

    /**
     * Returns the CSV column header.
     *
     * @return column header
     */
    String value();
}