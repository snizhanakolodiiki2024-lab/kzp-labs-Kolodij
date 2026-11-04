package ua.lpnu.kzp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Stores objects of one type in memory.
 *
 * @param <T> stored object type
 */
public final class Repository<T> {

    private final List<T> items = new ArrayList<>();

    /**
     * Adds a non-null item to the repository.
     *
     * @param item item to add
     * @throws NullPointerException if item is null
     */
    public void add(T item) {
        items.add(Objects.requireNonNull(item, "item must not be null"));
    }

    /**
     * Returns an immutable snapshot of all stored items.
     *
     * @return immutable snapshot of repository items
     */
    public List<T> all() {
        return List.copyOf(items);
    }

    /**
     * Finds all items that satisfy the specified condition.
     *
     * @param condition search condition
     * @return matching items
     * @throws NullPointerException if condition is null
     */
    public List<T> find(Predicate<? super T> condition) {
        Objects.requireNonNull(condition, "condition must not be null");

        return items.stream()
                .filter(condition)
                .toList();
    }
}