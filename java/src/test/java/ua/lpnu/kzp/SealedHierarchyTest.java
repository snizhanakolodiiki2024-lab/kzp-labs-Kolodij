package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests the sealed UtilityReading hierarchy and exhaustive pattern switch.
 */
class SealedHierarchyTest {

    @Test
    void describesElectricitySpecificState() {
        UtilityReading reading = new ElectricityReading(
                "Електролічильник-1",
                "2026-09-01",
                10.0,
                2.0,
                1.5
        );

        assertEquals(
                "tariffCoefficient=1.50",
                UtilityReading.describeSpecificState(reading)
        );
    }

    @Test
    void describesWaterSpecificState() {
        UtilityReading reading = new WaterReading(
                "Водомір-1",
                "2026-09-01",
                10.0,
                2.0,
                5.0
        );

        assertEquals(
                "serviceFee=5.00",
                UtilityReading.describeSpecificState(reading)
        );
    }
}