package ua.lpnu.kzp;

import java.util.Locale;

/**
 * Represents a common base type for utility meter readings.
 */
public abstract class UtilityReading {

    private final String meter;
    private final String date;
    private final double value;
    private final double tariff;

    /**
     * Creates a utility reading from validated common values.
     *
     * @param meter meter name
     * @param date reading date
     * @param value consumed value
     * @param tariff tariff for one unit
     */
    protected UtilityReading(
            String meter,
            String date,
            double value,
            double tariff) {

        this.meter = meter;
        this.date = date;
        this.value = value;
        this.tariff = tariff;
    }

    /**
     * Validates meter name.
     *
     * @param meter meter name
     * @return validated meter name
     * @throws IllegalArgumentException if meter is null or blank
     */
    protected static String validateMeter(String meter) {
        if (meter == null || meter.isBlank()) {
            throw new IllegalArgumentException(
                    "поле meter не може бути порожнім"
            );
        }

        return meter;
    }

    /**
     * Validates reading date.
     *
     * @param date reading date
     * @return validated date
     * @throws IllegalArgumentException if date is null or blank
     */
    protected static String validateDate(String date) {
        if (date == null || date.isBlank()) {
            throw new IllegalArgumentException(
                    "поле date не може бути порожнім"
            );
        }

        return date;
    }

    /**
     * Validates consumed value.
     *
     * @param value consumed value
     * @return validated value
     * @throws IllegalArgumentException if value is invalid
     */
    protected static double validateValue(double value) {
        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "value має бути скінченним числом"
            );
        }

        if (value < 0) {
            throw new IllegalArgumentException(
                    "value не може бути від'ємним"
            );
        }

        return value;
    }

    /**
     * Validates tariff.
     *
     * @param tariff tariff for one unit
     * @return validated tariff
     * @throws IllegalArgumentException if tariff is invalid
     */
    protected static double validateTariff(double tariff) {
        if (!Double.isFinite(tariff)) {
            throw new IllegalArgumentException(
                    "tariff має бути скінченним числом"
            );
        }

        if (tariff < 0) {
            throw new IllegalArgumentException(
                    "tariff не може бути від'ємним"
            );
        }

        return tariff;
    }

    /**
     * Returns meter name.
     *
     * @return meter name
     */
    public final String getMeter() {
        return meter;
    }

    /**
     * Returns reading date.
     *
     * @return reading date
     */
    public final String getDate() {
        return date;
    }

    /**
     * Returns consumed value.
     *
     * @return consumed value
     */
    public final double getValue() {
        return value;
    }

    /**
     * Returns tariff.
     *
     * @return tariff for one unit
     */
    public final double getTariff() {
        return tariff;
    }

    /**
     * Returns the utility category.
     *
     * @return utility kind
     */
    public abstract UtilityKind getKind();

    /**
     * Calculates the cost of this utility consumption.
     *
     * @return calculated consumption cost
     */
    public abstract double calculateCost();

    /**
     * Creates a utility reading from one CSV line.
     *
     * @param line CSV line in the format meter;date;value;tariff
     * @return created utility reading
     * @throws IllegalArgumentException if the line is invalid
     */
    public static UtilityReading fromCsv(String line) {
        if (line == null) {
            throw new IllegalArgumentException(
                    "рядок не може бути null"
            );
        }

        if (line.isBlank()) {
            throw new IllegalArgumentException(
                    "порожній рядок"
            );
        }

        String[] parts = line.split(";", -1);

        if (parts.length != 4) {
            throw new IllegalArgumentException(
                    "очікується 4 поля"
            );
        }

        try {
            String meter = parts[0].trim();
            String date = parts[1].trim();
            double value = Double.parseDouble(parts[2].trim());
            double tariff = Double.parseDouble(parts[3].trim());

            return createReading(
                    meter,
                    date,
                    value,
                    tariff
            );
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException(
                    "value і tariff мають бути числами",
                    exception
            );
        }
    }

    /**
     * Creates a concrete utility reading according to meter type.
     *
     * @param meter meter name
     * @param date reading date
     * @param value consumed value
     * @param tariff tariff for one unit
     * @return concrete utility reading
     * @throws IllegalArgumentException if meter type is unsupported
     */
    private static UtilityReading createReading(
            String meter,
            String date,
            double value,
            double tariff) {

        if (meter != null
                && meter.startsWith("Електролічильник")) {

            return new ElectricityReading(
                    meter,
                    date,
                    value,
                    tariff
            );
        }

        if (meter != null
                && meter.startsWith("Водомір")) {

            return new WaterReading(
                    meter,
                    date,
                    value,
                    tariff
            );
        }

        if (meter == null || meter.isBlank()) {
            throw new IllegalArgumentException(
                    "поле meter не може бути порожнім"
            );
        }

        throw new IllegalArgumentException(
                "невідомий тип лічильника"
        );
    }

    /**
     * Creates a new builder for utility readings.
     *
     * @return new builder
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * Builder for creating concrete UtilityReading objects.
     */
    public static final class Builder {

        private String meter;
        private String date;
        private double value;
        private double tariff;

        /**
         * Sets meter.
         *
         * @param meter meter name
         * @return this builder
         */
        public Builder meter(String meter) {
            this.meter = meter;
            return this;
        }

        /**
         * Sets date.
         *
         * @param date reading date
         * @return this builder
         */
        public Builder date(String date) {
            this.date = date;
            return this;
        }

        /**
         * Sets value.
         *
         * @param value consumed value
         * @return this builder
         */
        public Builder value(double value) {
            this.value = value;
            return this;
        }

        /**
         * Sets tariff.
         *
         * @param tariff tariff for one unit
         * @return this builder
         */
        public Builder tariff(double tariff) {
            this.tariff = tariff;
            return this;
        }

        /**
         * Creates a concrete UtilityReading using common validation.
         *
         * @return created utility reading
         * @throws IllegalArgumentException if any value is invalid
         */
        public UtilityReading build() {
            return createReading(
                    meter,
                    date,
                    value,
                    tariff
            );
        }
    }

    /**
     * Returns a string representation of this utility reading.
     *
     * @return formatted utility reading
     */
    @Override
    public String toString() {
        return String.format(
                Locale.ROOT,
                "UtilityReading{meter='%s', date='%s', value=%.2f, tariff=%.2f}",
                meter,
                date,
                value,
                tariff
        );
    }
}