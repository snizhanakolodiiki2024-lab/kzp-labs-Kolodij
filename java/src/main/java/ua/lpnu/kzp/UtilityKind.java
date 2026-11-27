package ua.lpnu.kzp;

/**
 * Represents a utility reading category.
 */
public enum UtilityKind {

    /**
     * Electricity meter reading.
     */
    ELECTRICITY("електроенергія"),

    /**
     * Water meter reading.
     */
    WATER("вода");

    private final String label;

    /**
     * Creates a utility kind with a display label.
     *
     * @param label display label
     */
    UtilityKind(String label) {
        this.label = label;
    }

    /**
     * Returns the display label.
     *
     * @return utility kind label
     */
    public String label() {
        return label;
    }
}