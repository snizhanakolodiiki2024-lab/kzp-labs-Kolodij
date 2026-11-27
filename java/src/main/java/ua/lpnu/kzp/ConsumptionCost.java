package ua.lpnu.kzp;

/**
 * Represents an immutable pair of consumption and its cost.
 *
 * @param consumption consumption value
 * @param cost cost of consumption
 */
public record ConsumptionCost(double consumption, double cost) {

    /**
     * Validates consumption and cost values.
     */
    public ConsumptionCost {
        if (!Double.isFinite(consumption) || consumption < 0) {
            throw new IllegalArgumentException(
                    "consumption має бути скінченним невід'ємним числом"
            );
        }

        if (!Double.isFinite(cost) || cost < 0) {
            throw new IllegalArgumentException(
                    "cost має бути скінченним невід'ємним числом"
            );
        }
    }
}