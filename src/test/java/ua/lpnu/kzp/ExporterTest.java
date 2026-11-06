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
 * Tests the common exporter contract.
 */
class ExporterTest {

    @TempDir
    Path temporaryDirectory;

    @Test
    void csvAndJsonUseSameExporterContract() throws Exception {
        UtilityReadingRecord record =
                new UtilityReadingRecord(
                        "Лічильник-1",
                        LocalDate.of(2026, 11, 5),
                        42.75,
                        5.10
                );

        List<UtilityReadingRecord> records =
                List.of(record);

        Exporter<UtilityReadingRecord> csvExporter =
                new CsvExporter<>();

        Exporter<UtilityReadingRecord> jsonExporter =
                new JsonExporter<>();

        Path csvPath =
                temporaryDirectory.resolve("readings.csv");

        Path jsonPath =
                temporaryDirectory.resolve("readings.json");

        csvExporter.export(
                csvPath,
                UtilityReadingRecord.class,
                records
        );

        jsonExporter.export(
                jsonPath,
                UtilityReadingRecord.class,
                records
        );

        String csv = Files.readString(
                csvPath,
                StandardCharsets.UTF_8
        );

        String json = Files.readString(
                jsonPath,
                StandardCharsets.UTF_8
        );

        assertTrue(csv.contains("Лічильник-1"));
        assertTrue(json.contains("Лічильник-1"));
    }
}