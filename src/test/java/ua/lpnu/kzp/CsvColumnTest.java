package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * Tests CsvColumn annotation metadata.
 */
class CsvColumnTest {

    @Test
    void utilityReadingRecordFieldsHaveCsvColumnAnnotations()
            throws NoSuchFieldException {

        Map<String, String> expectedHeaders = Map.of(
                "meter", "лічильник",
                "date", "дата",
                "value", "значення",
                "tariff", "тариф"
        );

        for (Map.Entry<String, String> entry
                : expectedHeaders.entrySet()) {

            Field field = UtilityReadingRecord.class
                    .getDeclaredField(entry.getKey());

            CsvColumn annotation =
                    field.getAnnotation(CsvColumn.class);

            assertNotNull(annotation);
            assertEquals(
                    entry.getValue(),
                    annotation.value()
            );
        }
    }
}