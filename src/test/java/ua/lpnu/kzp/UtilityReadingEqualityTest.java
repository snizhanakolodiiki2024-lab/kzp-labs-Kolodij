package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

/**
 * Tests logical equality and hash codes of utility readings.
 */
class UtilityReadingEqualityTest {

    @Test
    void equalElectricityReadingsAreEqual() {
        UtilityReading first = new ElectricityReading(
                "Електролічильник-1",
                "2026-09-01",
                10.0,
                2.0,
                1.5
        );

        UtilityReading second = new ElectricityReading(
                "Електролічильник-1",
                "2026-09-01",
                10.0,
                2.0,
                1.5
        );

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void differentElectricityCoefficientMakesReadingsUnequal() {
        UtilityReading first = new ElectricityReading(
                "Електролічильник-1",
                "2026-09-01",
                10.0,
                2.0,
                1.0
        );

        UtilityReading second = new ElectricityReading(
                "Електролічильник-1",
                "2026-09-01",
                10.0,
                2.0,
                1.5
        );

        assertNotEquals(first, second);
    }

    @Test
    void differentWaterServiceFeeMakesReadingsUnequal() {
        UtilityReading first = new WaterReading(
                "Водомір-1",
                "2026-09-01",
                10.0,
                2.0,
                0.0
        );

        UtilityReading second = new WaterReading(
                "Водомір-1",
                "2026-09-01",
                10.0,
                2.0,
                5.0
        );

        assertNotEquals(first, second);
    }

    @Test
    void differentSubtypesAreNotEqual() {
        UtilityReading electricity = new ElectricityReading(
                "Лічильник-1",
                "2026-09-01",
                10.0,
                2.0
        );

        UtilityReading water = new WaterReading(
                "Лічильник-1",
                "2026-09-01",
                10.0,
                2.0
        );

        assertNotEquals(electricity, water);
    }

    @Test
    void hashSetDoesNotStoreEqualDuplicates() {
        Set<UtilityReading> readings = new HashSet<>();

        readings.add(
                new WaterReading(
                        "Водомір-1",
                        "2026-09-01",
                        10.0,
                        2.0,
                        5.0
                )
        );

        readings.add(
                new WaterReading(
                        "Водомір-1",
                        "2026-09-01",
                        10.0,
                        2.0,
                        5.0
                )
        );

        assertEquals(1, readings.size());
    }
}