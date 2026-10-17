package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests polymorphic processing of utility readings.
 */
class PolymorphismTest {

    @Test
    void calculatesCostsThroughBaseType() {
        List<UtilityReading> readings = List.of(
                new ElectricityReading(
                        "Електролічильник-1",
                        "2026-09-01",
                        10.0,
                        2.0,
                        1.5
                ),
                new WaterReading(
                        "Водомір-1",
                        "2026-09-02",
                        10.0,
                        2.0,
                        5.0
                )
        );

        double totalCost = 0.0;

        for (UtilityReading reading : readings) {
            totalCost += reading.calculateCost();
        }

        assertEquals(
                55.0,
                totalCost,
                0.0001
        );
    }
}