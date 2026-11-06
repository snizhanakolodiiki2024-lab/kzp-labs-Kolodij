package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Compares reflection field lookup with and without cache.
 */
class ExportFieldCachePerformanceTest {

    private static final int ITERATIONS = 10_000;

    @Test
    void comparesReflectionLookupWithAndWithoutCache()
            throws Exception {

        long withoutCacheStart = System.nanoTime();

        int withoutCacheFieldCount = 0;

        for (int i = 0; i < ITERATIONS; i++) {
            List<Field> fields =
                    Arrays.stream(
                                    UtilityReadingRecord.class
                                            .getDeclaredFields()
                            )
                            .filter(field ->
                                    field.isAnnotationPresent(
                                            CsvColumn.class
                                    )
                            )
                            .sorted(
                                    Comparator.comparing(
                                            Field::getName
                                    )
                            )
                            .toList();

            withoutCacheFieldCount = fields.size();
        }

        long withoutCacheNanos =
                System.nanoTime() - withoutCacheStart;

        ExportFieldCache.fieldsFor(
                UtilityReadingRecord.class
        );

        long withCacheStart = System.nanoTime();

        int withCacheFieldCount = 0;

        for (int i = 0; i < ITERATIONS; i++) {
            List<Field> fields =
                    ExportFieldCache.fieldsFor(
                            UtilityReadingRecord.class
                    );

            withCacheFieldCount = fields.size();
        }

        long withCacheNanos =
                System.nanoTime() - withCacheStart;

        assertEquals(4, withoutCacheFieldCount);
        assertEquals(4, withCacheFieldCount);

        System.out.println(
                "Reflection without cache: "
                        + withoutCacheNanos
                        + " ns"
        );

        System.out.println(
                "Reflection with cache: "
                        + withCacheNanos
                        + " ns"
        );
    }
}