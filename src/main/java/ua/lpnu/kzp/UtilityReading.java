package ua.lpnu.kzp;

import java.util.Locale;

/**
 * Represents one utility meter reading.
 */
public final class UtilityReading {

    private final String meter;
    private final String date;
    private final double value;
    private final double tariff;

    /**
     * Creates a utility reading.
     *
     * @param meter meter name
     * @param date reading date
     * @param value consumed value
     * @param tariff tariff for one unit
     * @throws IllegalArgumentException if any value is invalid
     */
    public UtilityReading(String meter, String date, double value, double tariff) {
        if (meter == null || meter.isBlank()) {
    throw new IllegalArgumentException(
            "поле meter не може бути порожнім"
    );
}

if (date == null || date.isBlank()) {
    throw new IllegalArgumentException(
            "поле date не може бути порожнім"
    );
}

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

        this.meter = meter;
        this.date = date;
        this.value = value;
        this.tariff = tariff;
    }

    /**
     * Returns the meter name.
     *
     * @return meter name
     */
    public String getMeter() {
        return meter;
    }

    /**
     * Returns the reading date.
     *
     * @return reading date
     */
    public String getDate() {
        return date;
    }

    /**
     * Returns the consumed value.
     *
     * @return consumed value
     */
    public double getValue() {
        return value;
    }

    /**
     * Returns the tariff.
     *
     * @return tariff
     */
    public double getTariff() {
        return tariff;
    }

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

        return new UtilityReading(
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