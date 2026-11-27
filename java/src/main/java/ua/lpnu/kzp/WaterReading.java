package ua.lpnu.kzp;

/**
 * Represents a water meter reading.
 */
public final class WaterReading extends UtilityReading {

    private static final double DEFAULT_SERVICE_FEE = 0.0;

    private final double serviceFee;

    /**
     * Creates a water reading without an additional service fee.
     *
     * @param meter meter name
     * @param date reading date
     * @param value consumed value
     * @param tariff tariff for one unit
     */
    public WaterReading(
            String meter,
            String date,
            double value,
            double tariff) {

        this(
                meter,
                date,
                value,
                tariff,
                DEFAULT_SERVICE_FEE
        );
    }

    /**
     * Creates a water reading with an additional service fee.
     *
     * @param meter meter name
     * @param date reading date
     * @param value consumed value
     * @param tariff tariff for one unit
     * @param serviceFee additional service fee
     * @throws IllegalArgumentException if the service fee is invalid
     */
    public WaterReading(
            String meter,
            String date,
            double value,
            double tariff,
            double serviceFee) {

        super(
                validateMeter(meter),
                validateDate(date),
                validateValue(value),
                validateTariff(tariff)
        );

        if (!Double.isFinite(serviceFee)) {
            throw new IllegalArgumentException(
                    "serviceFee має бути скінченним числом"
            );
        }

        if (serviceFee < 0) {
            throw new IllegalArgumentException(
                    "serviceFee не може бути від'ємним"
            );
        }

        this.serviceFee = serviceFee;
    }

    /**
     * Returns additional water service fee.
     *
     * @return service fee
     */
    public double getServiceFee() {
        return serviceFee;
    }

    /**
     * Returns utility kind.
     *
     * @return water kind
     */
    @Override
    public UtilityKind getKind() {
        return UtilityKind.WATER;
    }

    /**
     * Calculates water consumption cost including the service fee.
     *
     * @return calculated cost
     */
    @Override
    public double calculateCost() {
        return getValue()
                * getTariff()
                + serviceFee;
    }

    /**
     * Compares water-specific state.
     *
     * @param other another water reading
     * @return true if service fees are equal
     */
    @Override
    protected boolean hasSameSpecificState(
            UtilityReading other) {

        WaterReading waterReading =
                (WaterReading) other;

        return Double.compare(
                serviceFee,
                waterReading.serviceFee
        ) == 0;
    }

    /**
     * Returns hash code of water-specific state.
     *
     * @return hash code
     */
    @Override
    protected int specificHashCode() {
        return Double.hashCode(serviceFee);
    }
}