package ua.lpnu.kzp;

import java.util.List;
import java.util.Objects;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.DoubleSummaryStatistics;
import java.util.Comparator;
import java.util.Optional;
import java.time.LocalDate;

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

/**
 * Повертає зведену статистику для значень споживання.
 *
 * @param readings вхідна колекція показників
 * @return статистика значень споживання
 * @throws NullPointerException якщо readings дорівнює null
 */
public static DoubleSummaryStatistics valueStatistics(
        List<UtilityReading> readings) {

    Objects.requireNonNull(readings, "readings must not be null");

    return readings.stream()
            .collect(Collectors.summarizingDouble(
                    UtilityReading::getValue
            ));
}

/**
 * Повертає перші N показників, відсортованих за значенням
 * споживання за спаданням і датою за зростанням.
 *
 * @param readings вхідна колекція показників
 * @param n максимальна кількість елементів
 * @return відсортований список перших N показників
 * @throws NullPointerException якщо readings дорівнює null
 * @throws IllegalArgumentException якщо n від'ємне
 */
public static List<UtilityReading> topByValue(
        List<UtilityReading> readings,
        int n) {

    Objects.requireNonNull(readings, "readings must not be null");

    if (n < 0) {
        throw new IllegalArgumentException("n must not be negative");
    }

    Comparator<UtilityReading> comparator =
        Comparator.comparingDouble(UtilityReading::getValue)
                .reversed()
                .thenComparing(
                        reading -> LocalDate.parse(reading.getDate())
                );

    return readings.stream()
            .sorted(comparator)
            .limit(n)
            .toList();
}

/**
 * Шукає перший показник із заданою назвою лічильника.
 *
 * @param readings вхідна колекція показників
 * @param meter назва лічильника
 * @return Optional із знайденим показником або Optional.empty()
 * @throws NullPointerException якщо readings або meter дорівнює null
 */
public static Optional<UtilityReading> findByMeter(
        List<UtilityReading> readings,
        String meter) {

    Objects.requireNonNull(readings, "readings must not be null");
    Objects.requireNonNull(meter, "meter must not be null");

    return readings.stream()
            .filter(reading -> reading.getMeter().equals(meter))
            .findFirst();
}

/**
 * Групує показники спочатку за категорією,
 * потім за назвою лічильника та обчислює
 * сумарне споживання для кожної групи.
 *
 * @param readings вхідна колекція показників
 * @return дворівнева карта category -> meter -> total consumption
 * @throws NullPointerException якщо readings дорівнює null
 */
public static Map<UtilityKind, Map<String, Double>> sumByKindAndMeter(
        List<UtilityReading> readings) {

    Objects.requireNonNull(readings, "readings must not be null");

    return readings.stream()
            .collect(Collectors.groupingBy(
                    UtilityReading::getKind,
                    Collectors.groupingBy(
                            UtilityReading::getMeter,
                            Collectors.summingDouble(
                                    UtilityReading::getValue
                            )
                    )
            ));
}
}