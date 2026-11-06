package ua.lpnu.kzp;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Caches fields annotated with CsvColumn for export operations.
 */
final class ExportFieldCache {

    private static final Map<Class<?>, List<Field>> CACHE =
            new ConcurrentHashMap<>();

    private ExportFieldCache() {
    }

    /**
     * Returns cached annotated fields in deterministic order.
     *
     * @param type object class
     * @return annotated fields
     * @throws DataStorageException if a field cannot be accessed
     */
    static List<Field> fieldsFor(Class<?> type)
            throws DataStorageException {

        List<Field> cached = CACHE.get(type);

        if (cached != null) {
            return cached;
        }

        List<Field> fields = Arrays.stream(type.getDeclaredFields())
                .filter(field ->
                        field.isAnnotationPresent(CsvColumn.class))
                .sorted(Comparator.comparing(Field::getName))
                .toList();

        for (Field field : fields) {
            if (!field.trySetAccessible()) {
                throw new DataStorageException(
                        "cannot access field: "
                                + field.getName()
                );
            }
        }

        CACHE.putIfAbsent(type, fields);

        return CACHE.get(type);
    }
}