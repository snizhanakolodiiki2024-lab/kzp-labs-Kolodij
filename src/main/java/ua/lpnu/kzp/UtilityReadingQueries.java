package ua.lpnu.kzp;

import java.util.List;
import java.util.Objects;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Містить потокові запити для колекції комунальних показників.
 */
public final class UtilityReadingQueries {

    private UtilityReadingQueries() {
    }

    /**
     * Повертає показники, значення яких перевищує заданий поріг.
     *
     * @param readings  вхідна колекція показників
     * @param threshold порогове значення
     * @return список показників зі значенням понад поріг
     * @throws NullPointerException якщо readings дорівнює null
     */
    public static List<UtilityReading> filterAboveThreshold(
            List<UtilityReading> readings,
            double threshold) {

        Objects.requireNonNull(readings, "readings must not be null");

        return readings.stream()
                .filter(reading -> reading.getValue() > threshold)
                .toList();
    }

    /**
 * Повертає значення споживання для всіх показників.
 *
 * @param readings вхідна колекція показників
 * @return список значень споживання
 * @throws NullPointerException якщо readings дорівнює null
 */
public static List<Double> consumptionValues(List<UtilityReading> readings) {
    Objects.requireNonNull(readings, "readings must not be null");

    return readings.stream()
            .map(UtilityReading::getValue)
            .toList();
}

/**
 * Повертає сумарне споживання для кожного лічильника.
 *
 * @param readings вхідна колекція показників
 * @return карта meter -> сумарне значення споживання
 * @throws NullPointerException якщо readings дорівнює null
 */
public static Map<String, Double> sumByMeter(List<UtilityReading> readings) {
    Objects.requireNonNull(readings, "readings must not be null");

    return readings.stream()
            .collect(Collectors.groupingBy(
                    UtilityReading::getMeter,
                    Collectors.summingDouble(UtilityReading::getValue)
            ));
}
}