package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link UtilityReading}.
 */
class UtilityReadingTest {

    @Test
    void constructorCreatesValidReading() {
        UtilityReading reading = new UtilityReading(
                "Водомір-1",
                "2026-09-01",
                10.0,
                2.5
        );

        assertEquals("Водомір-1", reading.getMeter());
        assertEquals("2026-09-01", reading.getDate());
        assertEquals(10.0, reading.getValue(), 0.0001);
        assertEquals(2.5, reading.getTariff(), 0.0001);
    }

    @Test
    void constructorAcceptsZeroValues() {
        UtilityReading reading = new UtilityReading(
                "Водомір-1",
                "2026-09-01",
                0.0,
                0.0
        );

        assertEquals(0.0, reading.getValue(), 0.0001);
        assertEquals(0.0, reading.getTariff(), 0.0001);
    }

    @Test
    void constructorRejectsNullMeter() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new UtilityReading(
                        null,
                        "2026-09-01",
                        10.0,
                        2.0
                )
        );
    }

    @Test
    void constructorRejectsBlankMeter() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new UtilityReading(
                        "   ",
                        "2026-09-01",
                        10.0,
                        2.0
                )
        );
    }

    @Test
    void constructorRejectsNullDate() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new UtilityReading(
                        "Водомір-1",
                        null,
                        10.0,
                        2.0
                )
        );
    }

    @Test
    void constructorRejectsBlankDate() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new UtilityReading(
                        "Водомір-1",
                        "   ",
                        10.0,
                        2.0
                )
        );
    }

    @Test
    void constructorRejectsNegativeValue() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new UtilityReading(
                        "Водомір-1",
                        "2026-09-01",
                        -1.0,
                        2.0
                )
        );
    }

    @Test
    void constructorRejectsNegativeTariff() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new UtilityReading(
                        "Водомір-1",
                        "2026-09-01",
                        10.0,
                        -1.0
                )
        );
    }

    @Test
    void constructorRejectsNanValue() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new UtilityReading(
                        "Водомір-1",
                        "2026-09-01",
                        Double.NaN,
                        2.0
                )
        );
    }

    @Test
    void constructorRejectsPositiveInfinityValue() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new UtilityReading(
                        "Водомір-1",
                        "2026-09-01",
                        Double.POSITIVE_INFINITY,
                        2.0
                )
        );
    }

    @Test
    void constructorRejectsNegativeInfinityValue() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new UtilityReading(
                        "Водомір-1",
                        "2026-09-01",
                        Double.NEGATIVE_INFINITY,
                        2.0
                )
        );
    }

    @Test
    void constructorRejectsNanTariff() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new UtilityReading(
                        "Водомір-1",
                        "2026-09-01",
                        10.0,
                        Double.NaN
                )
        );
    }

    @Test
    void constructorRejectsPositiveInfinityTariff() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new UtilityReading(
                        "Водомір-1",
                        "2026-09-01",
                        10.0,
                        Double.POSITIVE_INFINITY
                )
        );
    }

    @Test
    void constructorRejectsNegativeInfinityTariff() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new UtilityReading(
                        "Водомір-1",
                        "2026-09-01",
                        10.0,
                        Double.NEGATIVE_INFINITY
                )
        );
    }

    @Test
    void fromCsvCreatesValidReading() {
        UtilityReading reading = UtilityReading.fromCsv(
                "Водомір-1;2026-09-01;10.00;2.50"
        );

        assertEquals("Водомір-1", reading.getMeter());
        assertEquals("2026-09-01", reading.getDate());
        assertEquals(10.0, reading.getValue(), 0.0001);
        assertEquals(2.5, reading.getTariff(), 0.0001);
    }

    @Test
    void fromCsvRejectsWrongFieldCount() {
        assertThrows(
                IllegalArgumentException.class,
                () -> UtilityReading.fromCsv(
                        "Водомір-1;2026-09-01;10.00"
                )
        );
    }

    @Test
    void fromCsvRejectsInvalidNumber() {
        assertThrows(
                IllegalArgumentException.class,
                () -> UtilityReading.fromCsv(
                        "Водомір-1;2026-09-01;abc;2.50"
                )
        );
    }

    @Test
    void fromCsvRejectsInvariantViolation() {
        assertThrows(
                IllegalArgumentException.class,
                () -> UtilityReading.fromCsv(
                        "Водомір-1;2026-09-01;-1.00;2.50"
                )
        );
    }
}