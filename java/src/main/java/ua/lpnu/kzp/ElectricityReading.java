package ua.lpnu.kzp;

/**
 * Represents an electricity meter reading.
 */
public final class ElectricityReading extends UtilityReading {

    private static final double DEFAULT_TARIFF_COEFFICIENT = 1.0;

    private final double tariffCoefficient;

    /**
     * Creates an electricity reading with the default tariff coefficient.
     *
     * @param meter meter name
     * @param date reading date
     * @param value consumed value
     * @param tariff tariff for one unit
     */
    public ElectricityReading(
            String meter,
            String date,
            double value,
            double tariff) {

        this(
                meter,
                date,
                value,
                tariff,
                DEFAULT_TARIFF_COEFFICIENT
        );
    }

    /**
     * Creates an electricity reading with a tariff coefficient.
     *
     * @param meter meter name
     * @param date reading date
     * @param value consumed value
     * @param tariff tariff for one unit
     * @param tariffCoefficient electricity tariff coefficient
     * @throws IllegalArgumentException if the coefficient is invalid
     */
    public ElectricityReading(
            String meter,
            String date,
            double value,
            double tariff,
            double tariffCoefficient) {

        super(
                validateMeter(meter),
                validateDate(date),
                validateValue(value),
                validateTariff(tariff)
        );

        if (!Double.isFinite(tariffCoefficient)) {
            throw new IllegalArgumentException(
                    "tariffCoefficient має бути скінченним числом"
            );
        }

        if (tariffCoefficient <= 0) {
            throw new IllegalArgumentException(
                    "tariffCoefficient має бути додатним"
            );
        }

        this.tariffCoefficient = tariffCoefficient;
    }

    /**
     * Returns electricity tariff coefficient.
     *
     * @return tariff coefficient
     */
    public double getTariffCoefficient() {
        return tariffCoefficient;
    }

    /**
     * Returns utility kind.
     *
     * @return electricity kind
     */
    @Override
    public UtilityKind getKind() {
        return UtilityKind.ELECTRICITY;
    }

    /**
     * Calculates electricity consumption cost using a tariff coefficient.
     *
     * @return calculated cost
     */
    @Override
    public double calculateCost() {
        return getValue()
                * getTariff()
                * tariffCoefficient;
    }

    /**
     * Compares electricity-specific state.
     *
     * @param other another electricity reading
     * @return true if tariff coefficients are equal
     */
    @Override
    protected boolean hasSameSpecificState(
            UtilityReading other) {

        ElectricityReading electricityReading =
                (ElectricityReading) other;

        return Double.compare(
                tariffCoefficient,
                electricityReading.tariffCoefficient
        ) == 0;
    }

    /**
     * Returns hash code of electricity-specific state.
     *
     * @return hash code
     */
    @Override
    protected int specificHashCode() {
        return Double.hashCode(tariffCoefficient);
    }
}