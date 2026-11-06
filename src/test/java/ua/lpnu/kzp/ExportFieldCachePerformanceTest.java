package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Compares reflection and CSV export performance
 * with and without cached fields.
 */
class ExportFieldCachePerformanceTest {

    private static final int LOOKUP_ITERATIONS = 10_000;
    private static final int EXPORT_ITERATIONS = 50;
    private static final int RECORD_COUNT = 1_000;

    @TempDir
    Path temporaryDirectory;

    @Test
    void comparesReflectionLookupWithAndWithoutCache()
            throws Exception {

        long withoutCacheStart = System.nanoTime();

        int withoutCacheFieldCount = 0;

        for (int i = 0; i < LOOKUP_ITERATIONS; i++) {
            List<Field> fields =
                    findFieldsWithoutCache(
                            UtilityReadingRecord.class
                    );

            withoutCacheFieldCount = fields.size();
        }

        long withoutCacheNanos =
                System.nanoTime() - withoutCacheStart;

        ExportFieldCache.fieldsFor(
                UtilityReadingRecord.class
        );

        long withCacheStart = System.nanoTime();

        int withCacheFieldCount = 0;

        for (int i = 0; i < LOOKUP_ITERATIONS; i++) {
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

    @Test
    void comparesCsvExportWithAndWithoutCache()
            throws Exception {

        List<UtilityReadingRecord> records =
                createRecords();

        Path withoutCachePath =
                temporaryDirectory.resolve(
                        "without-cache.csv"
                );

        Path withCachePath =
                temporaryDirectory.resolve(
                        "with-cache.csv"
                );

        /*
         * Warm-up before measurement to reduce
         * the influence of JVM class loading and JIT.
         */
        for (int i = 0; i < 5; i++) {
            writeWithoutCache(
                    withoutCachePath,
                    UtilityReadingRecord.class,
                    records
            );

            CsvExporter.write(
                    withCachePath,
                    UtilityReadingRecord.class,
                    records
            );
        }

        long withoutCacheStart =
                System.nanoTime();

        for (int i = 0; i < EXPORT_ITERATIONS; i++) {
            writeWithoutCache(
                    withoutCachePath,
                    UtilityReadingRecord.class,
                    records
            );
        }

        long withoutCacheNanos =
                System.nanoTime() - withoutCacheStart;

        long withCacheStart =
                System.nanoTime();

        for (int i = 0; i < EXPORT_ITERATIONS; i++) {
            CsvExporter.write(
                    withCachePath,
                    UtilityReadingRecord.class,
                    records
            );
        }

        long withCacheNanos =
                System.nanoTime() - withCacheStart;

        String withoutCacheCsv =
                Files.readString(
                        withoutCachePath,
                        StandardCharsets.UTF_8
                );

        String withCacheCsv =
                Files.readString(
                        withCachePath,
                        StandardCharsets.UTF_8
                );

        assertEquals(
                withoutCacheCsv,
                withCacheCsv
        );

        double speedup =
                (double) withoutCacheNanos
                        / withCacheNanos;

        System.out.println(
                "CSV export without cache: "
                        + withoutCacheNanos
                        + " ns"
        );

        System.out.println(
                "CSV export with cache: "
                        + withCacheNanos
                        + " ns"
        );

        System.out.printf(
                Locale.ROOT,
                "CSV export cache ratio: %.2fx%n",
                speedup
        );
    }

    private static List<UtilityReadingRecord> createRecords() {
        return IntStream.range(0, RECORD_COUNT)
                .mapToObj(index ->
                        new UtilityReadingRecord(
                                "Лічильник-" + index,
                                LocalDate.of(
                                                2026,
                                                11,
                                                1
                                        )
                                        .plusDays(index % 28),
                                index + 0.5,
                                4.32
                        )
                )
                .toList();
    }

    private static List<Field> findFieldsWithoutCache(
            Class<?> type)
            throws DataStorageException {

        List<Field> fields =
                Arrays.stream(type.getDeclaredFields())
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

        for (Field field : fields) {
            if (!field.trySetAccessible()) {
                throw new DataStorageException(
                        "cannot access field: "
                                + field.getName()
                );
            }
        }

        return fields;
    }

    private static <T> void writeWithoutCache(
            Path path,
            Class<T> type,
            List<? extends T> records)
            throws Exception {

        List<Field> fields =
                findFieldsWithoutCache(type);

        String header = fields.stream()
                .map(field ->
                        field.getAnnotation(
                                CsvColumn.class
                        ).value()
                )
                .map(
                        ExportFieldCachePerformanceTest::escape
                )
                .collect(Collectors.joining(","));

        StringBuilder csv =
                new StringBuilder(header)
                        .append(
                                System.lineSeparator()
                        );

        for (T record : records) {
            List<String> values =
                    new ArrayList<>();

            for (Field field : fields) {
                Object value = field.get(record);

                values.add(
                        escape(
                                value == null
                                        ? ""
                                        : value.toString()
                        )
                );
            }

            csv.append(
                            String.join(",", values)
                    )
                    .append(
                            System.lineSeparator()
                    );
        }

        Files.writeString(
                path,
                csv.toString(),
                StandardCharsets.UTF_8
        );
    }

    private static String escape(String value) {
        boolean requiresQuotes =
                value.contains(",")
                        || value.contains("\"")
                        || value.contains("\n")
                        || value.contains("\r");

        String escaped =
                value.replace("\"", "\"\"");

        return requiresQuotes
                ? "\"" + escaped + "\""
                : escaped;
    }
}