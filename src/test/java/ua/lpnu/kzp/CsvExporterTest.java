package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the reflective CSV exporter.
 */
class CsvExporterTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    void writesHeaderAndValuesInDeterministicOrder()
            throws Exception {

        List<UtilityReadingRecord> records = List.of(
                new UtilityReadingRecord(
                        "Електролічильник-1",
                        LocalDate.of(2026, 11, 4),
                        120.5,
                        4.32
                ),
                new UtilityReadingRecord(
                        "Водомір-1",
                        LocalDate.of(2026, 11, 5),
                        50.0,
                        35.2
                )
        );

        Path path = temporaryDirectory.resolve("readings.csv");

        CsvExporter.write(
                path,
                UtilityReadingRecord.class,
                records
        );

        String actual = Files.readString(
                path,
                StandardCharsets.UTF_8
        );

        String expected =
                "дата,лічильник,тариф,значення"
                        + System.lineSeparator()
                        + "2026-11-04,Електролічильник-1,4.32,120.5"
                        + System.lineSeparator()
                        + "2026-11-05,Водомір-1,35.2,50.0"
                        + System.lineSeparator();

        assertEquals(expected, actual);
    }

    @Test
    void emptyListProducesHeaderOnly()
            throws Exception {

        Path path = temporaryDirectory.resolve("empty.csv");

        CsvExporter.write(
                path,
                UtilityReadingRecord.class,
                List.of()
        );

        String actual = Files.readString(
                path,
                StandardCharsets.UTF_8
        );

        String expected =
                "дата,лічильник,тариф,значення"
                        + System.lineSeparator();

        assertEquals(expected, actual);
    }

    @Test
    void rejectsClassWithoutCsvFields() {

        Path path = temporaryDirectory.resolve("invalid.csv");

        assertThrows(
                DataStorageException.class,
                () -> CsvExporter.write(
                        path,
                        String.class,
                        List.of()
                )
        );
    }

    @Test
void escapesCommaInField() throws Exception {
    Path path = temporaryDirectory.resolve("comma.csv");

    UtilityReadingRecord record =
            new UtilityReadingRecord(
                    "Лічильник, резервний",
                    LocalDate.of(2026, 11, 4),
                    120.5,
                    4.32
            );

    CsvExporter.write(
            path,
            UtilityReadingRecord.class,
            List.of(record)
    );

    String actual = Files.readString(
            path,
            StandardCharsets.UTF_8
    );

    String expected =
            "дата,лічильник,тариф,значення"
                    + System.lineSeparator()
                    + "2026-11-04,\"Лічильник, резервний\",4.32,120.5"
                    + System.lineSeparator();

    assertEquals(expected, actual);
}

@Test
void escapesQuotesInField() throws Exception {
    Path path = temporaryDirectory.resolve("quotes.csv");

    UtilityReadingRecord record =
            new UtilityReadingRecord(
                    "Лічильник \"нічний\"",
                    LocalDate.of(2026, 11, 4),
                    120.5,
                    4.32
            );

    CsvExporter.write(
            path,
            UtilityReadingRecord.class,
            List.of(record)
    );

    String actual = Files.readString(
            path,
            StandardCharsets.UTF_8
    );

    String expected =
            "дата,лічильник,тариф,значення"
                    + System.lineSeparator()
                    + "2026-11-04,\"Лічильник \"\"нічний\"\"\",4.32,120.5"
                    + System.lineSeparator();

    assertEquals(expected, actual);
}

@Test
void escapesMultilineField() throws Exception {
    Path path = temporaryDirectory.resolve("multiline.csv");

    UtilityReadingRecord record =
            new UtilityReadingRecord(
                    "Лічильник\nрезервний",
                    LocalDate.of(2026, 11, 4),
                    120.5,
                    4.32
            );

    CsvExporter.write(
            path,
            UtilityReadingRecord.class,
            List.of(record)
    );

    String actual = Files.readString(
            path,
            StandardCharsets.UTF_8
    );

    String expected =
            "дата,лічильник,тариф,значення"
                    + System.lineSeparator()
                    + "2026-11-04,\"Лічильник\nрезервний\",4.32,120.5"
                    + System.lineSeparator();

    assertEquals(expected, actual);
}

@Test
void preservesFileWriteErrorCause() {
    Path path = temporaryDirectory
            .resolve("missing")
            .resolve("readings.csv");

    UtilityReadingRecord record =
            new UtilityReadingRecord(
                    "Лічильник-1",
                    LocalDate.of(2026, 11, 4),
                    120.5,
                    4.32
            );

    DataStorageException exception =
            assertThrows(
                    DataStorageException.class,
                    () -> CsvExporter.write(
                            path,
                            UtilityReadingRecord.class,
                            List.of(record)
                    )
            );

    assertTrue(exception.getCause() instanceof java.io.IOException);
}
}