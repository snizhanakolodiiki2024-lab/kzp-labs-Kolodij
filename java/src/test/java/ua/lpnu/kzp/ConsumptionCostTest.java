package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ConsumptionCost}.
 */
class ConsumptionCostTest {

    @Test
    void createsValidConsumptionCost() {
        ConsumptionCost result = new ConsumptionCost(
                10.0,
                25.0
        );

        assertEquals(10.0, result.consumption(), 0.0001);
        assertEquals(25.0, result.cost(), 0.0001);
    }

    @Test
    void acceptsZeroValues() {
        ConsumptionCost result = new ConsumptionCost(
                0.0,
                0.0
        );

        assertEquals(0.0, result.consumption(), 0.0001);
        assertEquals(0.0, result.cost(), 0.0001);
    }

    @Test
    void usesValueEquality() {
        ConsumptionCost first = new ConsumptionCost(
                10.0,
                25.0
        );

        ConsumptionCost second = new ConsumptionCost(
                10.0,
                25.0
        );

        assertEquals(first, second);
    }

    @Test
    void rejectsNegativeConsumption() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ConsumptionCost(
                        -1.0,
                        25.0
                )
        );
    }

    @Test
    void rejectsNegativeCost() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ConsumptionCost(
                        10.0,
                        -1.0
                )
        );
    }

    @Test
    void rejectsNanConsumption() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ConsumptionCost(
                        Double.NaN,
                        25.0
                )
        );
    }

    @Test
    void rejectsInfiniteCost() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new ConsumptionCost(
                        10.0,
                        Double.POSITIVE_INFINITY
                )
        );
    }
}