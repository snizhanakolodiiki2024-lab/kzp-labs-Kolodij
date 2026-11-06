package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests reflection field caching.
 */
class ExportFieldCacheTest {

    @Test
    void returnsSameCachedFieldListForSameClass()
            throws Exception {

        List<Field> first =
                ExportFieldCache.fieldsFor(
                        UtilityReadingRecord.class
                );

        List<Field> second =
                ExportFieldCache.fieldsFor(
                        UtilityReadingRecord.class
                );

        assertSame(first, second);
        assertEquals(4, first.size());
    }
}