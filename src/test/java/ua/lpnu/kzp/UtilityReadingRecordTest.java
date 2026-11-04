package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import java.util.List;

/**
 * Tests UtilityReadingRecord.
 */
class UtilityReadingRecordTest {

    @Test
    void storesAllValues() {
        LocalDate date = LocalDate.of(2026, 11, 4);

        UtilityReadingRecord record =
                new UtilityReadingRecord(
                        "Електролічильник-1",
                        date,
                        120.5,
                        4.32
                );

        assertEquals("Електролічильник-1", record.getMeter());
        assertEquals(date, record.getDate());
        assertEquals(120.5, record.getValue());
        assertEquals(4.32, record.getTariff());
    }

    @Test
    void equalRecordsHaveEqualHashCodes() {
        UtilityReadingRecord first =
                new UtilityReadingRecord(
                        "Водомір-1",
                        LocalDate.of(2026, 11, 1),
                        50.0,
                        35.20
                );

        UtilityReadingRecord second =
                new UtilityReadingRecord(
                        "Водомір-1",
                        LocalDate.of(2026, 11, 1),
                        50.0,
                        35.20
                );

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void rejectsBlankMeter() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new UtilityReadingRecord(
                        " ",
                        LocalDate.of(2026, 11, 1),
                        10.0,
                        4.32
                )
        );
    }

    @Test
    void rejectsNullDate() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new UtilityReadingRecord(
                        "Електролічильник-1",
                        null,
                        10.0,
                        4.32
                )
        );
    }

    @Test
    void rejectsNegativeValue() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new UtilityReadingRecord(
                        "Електролічильник-1",
                        LocalDate.of(2026, 11, 1),
                        -1.0,
                        4.32
                )
        );
    }

    @Test
    void rejectsNegativeTariff() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new UtilityReadingRecord(
                        "Електролічильник-1",
                        LocalDate.of(2026, 11, 1),
                        10.0,
                        -1.0
                )
        );
    }

    @Test
void restoresRecordFromCsvFields() throws Exception {
    UtilityReadingRecord record =
            UtilityReadingRecord.fromCsvFields(
                    List.of(
                            "2026-11-04",
                            "Електролічильник-1",
                            "4.32",
                            "120.5"
                    )
            );

    assertEquals(
            new UtilityReadingRecord(
                    "Електролічильник-1",
                    LocalDate.of(2026, 11, 4),
                    120.5,
                    4.32
            ),
            record
    );
}

@Test
void rejectsWrongCsvFieldCount() {
    assertThrows(
            DataStorageException.class,
            () -> UtilityReadingRecord.fromCsvFields(
                    List.of(
                            "2026-11-04",
                            "Електролічильник-1",
                            "4.32"
                    )
            )
    );
}

@Test
void rejectsInvalidCsvNumberAndPreservesCause() {
    DataStorageException exception =
            assertThrows(
                    DataStorageException.class,
                    () -> UtilityReadingRecord.fromCsvFields(
                            List.of(
                                    "2026-11-04",
                                    "Електролічильник-1",
                                    "wrong-number",
                                    "120.5"
                            )
                    )
            );

    assertEquals(
            NumberFormatException.class,
            exception.getCause().getClass()
    );
}
}