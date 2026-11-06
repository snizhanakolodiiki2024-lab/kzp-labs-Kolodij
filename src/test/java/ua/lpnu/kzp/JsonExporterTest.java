package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests JsonExporter.
 */
class JsonExporterTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    void exportsAnnotatedFieldsToJson() throws Exception {
        UtilityReadingRecord record =
                new UtilityReadingRecord(
                        "Лічильник, \"нічний\"\nрезервний",
                        LocalDate.of(2026, 11, 5),
                        42.75,
                        5.10
                );

        Path path = temporaryDirectory.resolve("readings.json");

        Exporter<UtilityReadingRecord> exporter =
                new JsonExporter<>();

        exporter.export(
                path,
                UtilityReadingRecord.class,
                List.of(record)
        );

        String json = Files.readString(
                path,
                StandardCharsets.UTF_8
        );

        assertTrue(json.contains("\"дата\": \"2026-11-05\""));
        assertTrue(json.contains("\"тариф\": 5.1"));
        assertTrue(json.contains("\"значення\": 42.75"));
        assertTrue(
                json.contains(
                        "\"лічильник\": "
                                + "\"Лічильник, \\\"нічний\\\"\\nрезервний\""
                )
        );
    }
}