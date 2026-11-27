package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link UtilityKind}.
 */
class UtilityKindTest {

    @Test
    void containsExpectedKinds() {
        assertEquals(
                2,
                UtilityKind.values().length
        );

        assertEquals(
                UtilityKind.ELECTRICITY,
                UtilityKind.valueOf("ELECTRICITY")
        );

        assertEquals(
                UtilityKind.WATER,
                UtilityKind.valueOf("WATER")
        );
    }

    @Test
    void returnsDisplayLabels() {
        assertEquals(
                "електроенергія",
                UtilityKind.ELECTRICITY.label()
        );

        assertEquals(
                "вода",
                UtilityKind.WATER.label()
        );
    }
}