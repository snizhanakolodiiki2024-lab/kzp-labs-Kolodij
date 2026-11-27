package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link WaterReading}.
 */
class WaterReadingTest {

    @Test
    void calculatesCostIncludingServiceFee() {
        UtilityReading reading = new WaterReading(
                "Водомір-1",
                "2026-09-01",
                10.0,
                2.0,
                5.0
        );

        assertEquals(
                25.0,
                reading.calculateCost(),
                0.0001
        );
    }

    @Test
    void acceptsZeroServiceFee() {
        WaterReading reading = new WaterReading(
                "Водомір-1",
                "2026-09-01",
                10.0,
                2.0,
                0.0
        );

        assertEquals(
                0.0,
                reading.getServiceFee(),
                0.0001
        );

        assertEquals(
                20.0,
                reading.calculateCost(),
                0.0001
        );
    }

    @Test
    void returnsWaterKind() {
        UtilityReading reading = new WaterReading(
                "Водомір-1",
                "2026-09-01",
                10.0,
                2.0
        );

        assertEquals(
                UtilityKind.WATER,
                reading.getKind()
        );
    }

    @Test
    void rejectsNegativeServiceFee() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new WaterReading(
                        "Водомір-1",
                        "2026-09-01",
                        10.0,
                        2.0,
                        -1.0
                )
        );
    }

    @Test
    void rejectsNonFiniteServiceFee() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new WaterReading(
                        "Водомір-1",
                        "2026-09-01",
                        10.0,
                        2.0,
                        Double.POSITIVE_INFINITY
                )
        );
    }
}