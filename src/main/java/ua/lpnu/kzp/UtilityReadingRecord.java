package ua.lpnu.kzp;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a utility reading prepared for persistent storage.
 */
public final class UtilityReadingRecord {

    @CsvColumn("лічильник")
    private final String meter;

    @CsvColumn("дата")
    private final LocalDate date;

    @CsvColumn("значення")
    private final double value;

    @CsvColumn("тариф")
    private final double tariff;

    /**
     * Creates a utility reading record.
     *
     * @param meter meter name
     * @param date reading date
     * @param value consumption value
     * @param tariff utility tariff
     */
    public UtilityReadingRecord(
            String meter,
            LocalDate date,
            double value,
            double tariff) {

        if (meter == null || meter.isBlank()) {
            throw new IllegalArgumentException(
                    "meter must not be blank"
            );
        }

        if (date == null) {
            throw new IllegalArgumentException(
                    "date must not be null"
            );
        }

        if (value < 0) {
            throw new IllegalArgumentException(
                    "value must not be negative"
            );
        }

        if (tariff < 0) {
            throw new IllegalArgumentException(
                    "tariff must not be negative"
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
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the consumption value.
     *
     * @return consumption value
     */
    public double getValue() {
        return value;
    }

    /**
     * Returns the utility tariff.
     *
     * @return utility tariff
     */
    public double getTariff() {
        return tariff;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof UtilityReadingRecord other)) {
            return false;
        }

        return Double.compare(value, other.value) == 0
                && Double.compare(tariff, other.tariff) == 0
                && meter.equals(other.meter)
                && date.equals(other.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meter, date, value, tariff);
    }
}