package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.DoubleSummaryStatistics;
import java.util.Optional;

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

@Test
void valueStatisticsReturnsSummaryForAllReadings() {
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

    DoubleSummaryStatistics result =
            UtilityReadingQueries.valueStatistics(readings);

    assertEquals(3, result.getCount());
    assertEquals(250.0, result.getSum());
    assertEquals(50.0, result.getMin());
    assertEquals(120.0, result.getMax());
    assertEquals(250.0 / 3.0, result.getAverage(), 0.000001);
}

@Test
void valueStatisticsHandlesEmptyInput() {
    DoubleSummaryStatistics result =
            UtilityReadingQueries.valueStatistics(List.of());

    assertEquals(0, result.getCount());
    assertEquals(0.0, result.getSum());
    assertEquals(0.0, result.getAverage());
    assertEquals(
            Double.POSITIVE_INFINITY,
            result.getMin()
    );
    assertEquals(
            Double.NEGATIVE_INFINITY,
            result.getMax()
    );
}

@Test
void topByValueSortsByValueDescendingAndDateForEqualValues() {
    List<UtilityReading> readings = List.of(
            new ElectricityReading(
                    "Електролічильник 1",
                    "2026-10-03",
                    80.0,
                    2.0),
            new WaterReading(
                    "Водомір 1",
                    "2026-10-04",
                    120.0,
                    3.0),
            new ElectricityReading(
                    "Електролічильник 2",
                    "2026-10-02",
                    120.0,
                    2.5),
            new WaterReading(
                    "Водомір 2",
                    "2026-10-01",
                    50.0,
                    3.0)
    );

    List<UtilityReading> result =
            UtilityReadingQueries.topByValue(readings, 3);

    assertEquals(3, result.size());

    assertEquals(120.0, result.get(0).getValue());
    assertEquals("2026-10-02", result.get(0).getDate());

    assertEquals(120.0, result.get(1).getValue());
    assertEquals("2026-10-04", result.get(1).getDate());

    assertEquals(80.0, result.get(2).getValue());
}

@Test
void topByValueReturnsEmptyListWhenNIsZero() {
    List<UtilityReading> readings = List.of(
            new ElectricityReading(
                    "Електролічильник 1",
                    "2026-10-01",
                    50.0,
                    2.0)
    );

    List<UtilityReading> result =
            UtilityReadingQueries.topByValue(readings, 0);

    assertTrue(result.isEmpty());
}

@Test
void topByValueReturnsAllElementsWhenNExceedsSize() {
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
                    3.0)
    );

    List<UtilityReading> result =
            UtilityReadingQueries.topByValue(readings, 10);

    assertEquals(2, result.size());
    assertEquals(120.0, result.get(0).getValue());
    assertEquals(50.0, result.get(1).getValue());
}

@Test
void findByMeterReturnsMatchingReading() {
    UtilityReading expected = new WaterReading(
            "Водомір 1",
            "2026-10-02",
            120.0,
            3.0
    );

    List<UtilityReading> readings = List.of(
            new ElectricityReading(
                    "Електролічильник 1",
                    "2026-10-01",
                    50.0,
                    2.0),
            expected
    );

    Optional<UtilityReading> result =
            UtilityReadingQueries.findByMeter(
                    readings,
                    "Водомір 1"
            );

    assertTrue(result.isPresent());
    assertEquals(expected, result.get());
}

@Test
void findByMeterReturnsEmptyOptionalWhenMeterIsAbsent() {
    List<UtilityReading> readings = List.of(
            new ElectricityReading(
                    "Електролічильник 1",
                    "2026-10-01",
                    50.0,
                    2.0)
    );

    Optional<UtilityReading> result =
            UtilityReadingQueries.findByMeter(
                    readings,
                    "Водомір 99"
            );

    assertTrue(result.isEmpty());
}

@Test
void findByMeterReturnsFirstReadingWhenMeterIsDuplicated() {
    UtilityReading first = new ElectricityReading(
            "Електролічильник 1",
            "2026-10-01",
            50.0,
            2.0
    );

    UtilityReading second = new ElectricityReading(
            "Електролічильник 1",
            "2026-10-02",
            80.0,
            2.0
    );

    List<UtilityReading> readings = List.of(first, second);

    Optional<UtilityReading> result =
            UtilityReadingQueries.findByMeter(
                    readings,
                    "Електролічильник 1"
            );

    assertTrue(result.isPresent());
    assertEquals(first, result.get());
}

@Test
void streamQueriesDoNotModifyInputList() {
    UtilityReading first = new ElectricityReading(
            "Електролічильник 1",
            "2026-10-03",
            80.0,
            2.0
    );

    UtilityReading second = new WaterReading(
            "Водомір 1",
            "2026-10-01",
            120.0,
            3.0
    );

    UtilityReading third = new ElectricityReading(
            "Електролічильник 2",
            "2026-10-02",
            50.0,
            2.5
    );

    List<UtilityReading> readings =
            new java.util.ArrayList<>(List.of(first, second, third));

    List<UtilityReading> originalOrder =
            List.copyOf(readings);

    UtilityReadingQueries.filterAboveThreshold(readings, 60.0);
    UtilityReadingQueries.consumptionValues(readings);
    UtilityReadingQueries.sumByMeter(readings);
    UtilityReadingQueries.valueStatistics(readings);
    UtilityReadingQueries.topByValue(readings, 2);
    UtilityReadingQueries.findByMeter(readings, "Водомір 1");

    assertEquals(originalOrder, readings);
}
}