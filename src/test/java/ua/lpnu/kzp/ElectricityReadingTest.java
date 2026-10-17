package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ElectricityReading}.
 */
class ElectricityReadingTest {

    @Test
    void calculatesCostUsingTariffCoefficient() {
        UtilityReading reading = new ElectricityReading(
                "Електролічильник-1",
                "2026-09-01",
                10.0,
                2.0,
                1.5
        );

        assertEquals(
                30.0,
                reading.calculateCost(),
                0.0001
        );
    }

    @Test
    void defaultCoefficientPreservesBasicCost() {
        UtilityReading reading = new ElectricityReading(
                "Електролічильник-1",
                "2026-09-01",
                10.0,
                2.0
        );

        assertEquals(
                20.0,
                reading.calculateCost(),
                0.0001
        );
    }

    @Test
    void returnsElectricityKind() {
        UtilityReading reading = new ElectricityReading(
                "Електролічильник-1",
                "2026-09-01",
                10.0,
                2.0
        );

        assertEquals(
                UtilityKind.ELECTRICITY,
                reading.getKind()
        );
    }

    @Test
    void rejectsZeroTariffCoefficient() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ElectricityReading(
                        "Електролічильник-1",
                        "2026-09-01",
                        10.0,
                        2.0,
                        0.0
                )
        );
    }

    @Test
    void rejectsNonFiniteTariffCoefficient() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ElectricityReading(
                        "Електролічильник-1",
                        "2026-09-01",
                        10.0,
                        2.0,
                        Double.NaN
                )
        );
    }
}