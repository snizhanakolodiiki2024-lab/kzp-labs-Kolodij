package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests the generic repository.
 */
class RepositoryTest {

    @Test
    void addStoresItem() {
        Repository<String> repository = new Repository<>();

        repository.add("first");

        assertEquals(List.of("first"), repository.all());
    }

    @Test
    void allReturnsImmutableSnapshot() {
        Repository<String> repository = new Repository<>();
        repository.add("first");

        List<String> snapshot = repository.all();

        repository.add("second");

        assertEquals(List.of("first"), snapshot);
        assertThrows(
                UnsupportedOperationException.class,
                () -> snapshot.add("third")
        );
    }

    @Test
    void findReturnsMatchingItems() {
        Repository<Integer> repository = new Repository<>();
        repository.add(10);
        repository.add(20);
        repository.add(30);

        List<Integer> result = repository.find(value -> value >= 20);

        assertEquals(List.of(20, 30), result);
    }

    @Test
    void addRejectsNull() {
        Repository<String> repository = new Repository<>();

        assertThrows(
                NullPointerException.class,
                () -> repository.add(null)
        );
    }

    @Test
    void findRejectsNullCondition() {
        Repository<String> repository = new Repository<>();

        assertThrows(
                NullPointerException.class,
                () -> repository.find(null)
        );
    }
}