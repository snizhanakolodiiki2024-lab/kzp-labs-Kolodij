package ua.lpnu.kzp;

import java.time.LocalDate;
import java.util.List;
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
     * @throws IllegalArgumentException if record values are invalid
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

        if (!Double.isFinite(value)) {
            throw new IllegalArgumentException(
                    "value must be finite"
            );
        }

        if (value < 0) {
            throw new IllegalArgumentException(
                    "value must not be negative"
            );
        }

        if (!Double.isFinite(tariff)) {
            throw new IllegalArgumentException(
                    "tariff must be finite"
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

    /**
     * Creates a utility reading record from one CSV record.
     *
     * @param line CSV record
     * @return restored utility reading record
     * @throws DataStorageException if CSV data is invalid
     */
    public static UtilityReadingRecord fromCsv(String line)
            throws DataStorageException {

        List<List<String>> rows = CsvParser.parse(line);

        if (rows.size() != 1) {
            throw new DataStorageException(
                    "expected exactly one CSV record but got "
                            + rows.size()
            );
        }

        return fromCsvFields(rows.get(0));
    }

    /**
     * Restores a utility reading record from CSV fields.
     *
     * @param fields CSV fields in deterministic exporter order:
     *               date, meter, tariff, value
     * @return restored utility reading record
     * @throws DataStorageException if field count or field values are invalid
     */
    public static UtilityReadingRecord fromCsvFields(
            List<String> fields)
            throws DataStorageException {

        if (fields == null) {
            throw new DataStorageException(
                    "CSV fields must not be null"
            );
        }

        if (fields.size() != 4) {
            throw new DataStorageException(
                    "expected 4 CSV fields but got "
                            + fields.size()
            );
        }

        try {
            LocalDate date =
                    LocalDate.parse(fields.get(0));

            String meter =
                    fields.get(1);

            double tariff =
                    Double.parseDouble(fields.get(2));

            double value =
                    Double.parseDouble(fields.get(3));

            return new UtilityReadingRecord(
                    meter,
                    date,
                    value,
                    tariff
            );
        } catch (RuntimeException exception) {
            throw new DataStorageException(
                    "invalid utility reading CSV values",
                    exception
            );
        }
    }

    /**
     * Compares utility reading records by all stored fields.
     *
     * @param object object to compare with
     * @return true if all stored fields are equal
     */
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

    /**
     * Returns a hash code consistent with equals.
     *
     * @return hash code of this record
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                meter,
                date,
                value,
                tariff
        );
    }
}