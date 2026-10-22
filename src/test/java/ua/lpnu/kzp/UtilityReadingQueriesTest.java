package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import java.util.Map;

class UtilityReadingQueriesTest {

    @Test
    void filterAboveThresholdReturnsOnlyReadingsAboveThreshold() {
        List<UtilityReading> readings = List.of(
                new ElectricityReading(
                        "Електролічильник 1",
                        "2026-10-01",
                        50.0,
                        2.0),
                new WaterReading(
                        "Водомір 1",
                        "2026-10-02",
                        120.0,
                        3.0),
                new ElectricityReading(
                        "Електролічильник 2",
                        "2026-10-03",
                        80.0,
                        2.5)
        );

        List<UtilityReading> result =
                UtilityReadingQueries.filterAboveThreshold(readings, 75.0);

        assertEquals(2, result.size());
        assertEquals(120.0, result.get(0).getValue());
        assertEquals(80.0, result.get(1).getValue());
    }

    @Test
    void filterAboveThresholdExcludesValueEqualToThreshold() {
        List<UtilityReading> readings = List.of(
                new ElectricityReading(
                        "Електролічильник 1",
                        "2026-10-01",
                        75.0,
                        2.0),
                new WaterReading(
                        "Водомір 1",
                        "2026-10-02",
                        76.0,
                        3.0)
        );

        List<UtilityReading> result =
                UtilityReadingQueries.filterAboveThreshold(readings, 75.0);

        assertEquals(1, result.size());
        assertEquals(76.0, result.get(0).getValue());
    }

    @Test
    void filterAboveThresholdReturnsEmptyListForEmptyInput() {
        List<UtilityReading> result =
                UtilityReadingQueries.filterAboveThreshold(List.of(), 75.0);

        assertTrue(result.isEmpty());
    }

    @Test
void consumptionValuesReturnsValuesForAllReadings() {
    List<UtilityReading> readings = List.of(
            new ElectricityReading(
                    "Електролічильник 1",
                    "2026-10-01",
                    50.0,
                    2.0),
            new WaterReading(
                    "Водомір 1",
                    "2026-10-02",
                    120.0,
                    3.0),
            new ElectricityReading(
                    "Електролічильник 2",
                    "2026-10-03",
                    80.0,
                    2.5)
    );

    List<Double> result =
            UtilityReadingQueries.consumptionValues(readings);

    assertEquals(List.of(50.0, 120.0, 80.0), result);
}

@Test
void consumptionValuesReturnsEmptyListForEmptyInput() {
    List<Double> result =
            UtilityReadingQueries.consumptionValues(List.of());

    assertTrue(result.isEmpty());
}

@Test
void sumByMeterReturnsTotalConsumptionForEachMeter() {
    List<UtilityReading> readings = List.of(
            new ElectricityReading(
                    "Електролічильник 1",
                    "2026-10-01",
                    50.0,
                    2.0),
            new ElectricityReading(
                    "Електролічильник 1",
                    "2026-10-02",
                    30.0,
                    2.0),
            new WaterReading(
                    "Водомір 1",
                    "2026-10-03",
                    20.0,
                    3.0)
    );

    Map<String, Double> result =
            UtilityReadingQueries.sumByMeter(readings);

    assertEquals(2, result.size());
    assertEquals(80.0, result.get("Електролічильник 1"));
    assertEquals(20.0, result.get("Водомір 1"));
}

@Test
void sumByMeterReturnsEmptyMapForEmptyInput() {
    Map<String, Double> result =
            UtilityReadingQueries.sumByMeter(List.of());

    assertTrue(result.isEmpty());
}
}