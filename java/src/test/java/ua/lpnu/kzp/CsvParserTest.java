package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests CsvParser.
 */
class CsvParserTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    void parsesSimpleCsv() throws Exception {
        String csv =
                "a,b,c\n"
                        + "1,2,3\n";

        List<List<String>> rows = CsvParser.parse(csv);

        assertEquals(
                List.of(
                        List.of("a", "b", "c"),
                        List.of("1", "2", "3")
                ),
                rows
        );
    }

    @Test
    void parsesQuotedComma() throws Exception {
        String csv =
                "name,value\n"
                        + "\"Лічильник, резервний\",120.5\n";

        List<List<String>> rows = CsvParser.parse(csv);

        assertEquals(
                "Лічильник, резервний",
                rows.get(1).get(0)
        );
    }

    @Test
    void parsesEscapedQuote() throws Exception {
        String csv =
                "name\n"
                        + "\"Лічильник \"\"нічний\"\"\"\n";

        List<List<String>> rows = CsvParser.parse(csv);

        assertEquals(
                "Лічильник \"нічний\"",
                rows.get(1).get(0)
        );
    }

    @Test
    void parsesMultilineField() throws Exception {
        String csv =
                "name,value\n"
                        + "\"Лічильник\nрезервний\",120.5\n";

        List<List<String>> rows = CsvParser.parse(csv);

        assertEquals(2, rows.size());
        assertEquals(
                "Лічильник\nрезервний",
                rows.get(1).get(0)
        );
        assertEquals("120.5", rows.get(1).get(1));
    }

    @Test
    void parsesCrLfLineSeparators() throws Exception {
        String csv =
                "a,b\r\n"
                        + "1,2\r\n";

        List<List<String>> rows = CsvParser.parse(csv);

        assertEquals(
                List.of(
                        List.of("a", "b"),
                        List.of("1", "2")
                ),
                rows
        );
    }

    @Test
    void emptyCsvProducesNoRows() throws Exception {
        assertEquals(
                List.of(),
                CsvParser.parse("")
        );
    }

    @Test
    void rejectsUnclosedQuotedField() {
        DataStorageException exception =
                assertThrows(
                        DataStorageException.class,
                        () -> CsvParser.parse(
                                "name\n\"broken"
                        )
                );

        assertTrue(
                exception.getMessage().contains("unclosed")
        );
    }

    @Test
    void preservesFileErrorCause() {
        Path path = temporaryDirectory
                .resolve("missing")
                .resolve("readings.csv");

        DataStorageException exception =
                assertThrows(
                        DataStorageException.class,
                        () -> CsvParser.read(path)
                );

        assertTrue(exception.getCause() instanceof IOException);
    }

    @Test
void utilityReadingRecordRoundTripWorks() throws Exception {
    UtilityReadingRecord original =
            new UtilityReadingRecord(
                    "Лічильник, \"нічний\"\nрезервний",
                    java.time.LocalDate.of(2026, 11, 4),
                    120.5,
                    4.32
            );

    Path path = temporaryDirectory.resolve("round-trip.csv");

    CsvExporter.write(
            path,
            UtilityReadingRecord.class,
            List.of(original)
    );

    List<List<String>> rows = CsvParser.read(path);

    assertEquals(2, rows.size());

    UtilityReadingRecord restored =
            UtilityReadingRecord.fromCsvFields(
                    rows.get(1)
            );

    assertEquals(original, restored);
}
}