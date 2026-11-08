package ua.lpnu.kzp;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Caches fields annotated with CsvColumn for export operations.
 *
 * <p>Annotated fields are collected from the specified class
 * and all of its superclasses except Object.</p>
 */
final class ExportFieldCache {

    private static final Map<Class<?>, List<Field>> CACHE =
            new ConcurrentHashMap<>();

    private ExportFieldCache() {
    }

    /**
     * Returns cached annotated fields from the class hierarchy
     * in deterministic order.
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

        List<Field> fields = new ArrayList<>();

        Class<?> currentType = type;

        while (currentType != null
                && currentType != Object.class) {

            for (Field field
                    : currentType.getDeclaredFields()) {

                if (field.isAnnotationPresent(
                        CsvColumn.class)) {

                    fields.add(field);
                }
            }

            currentType = currentType.getSuperclass();
        }

        fields.sort(
                Comparator.comparing(Field::getName)
                        .thenComparing(
                                field ->
                                        field.getDeclaringClass()
                                                .getName()
                        )
        );

        for (Field field : fields) {
            if (!field.trySetAccessible()) {
                throw new DataStorageException(
                        "cannot access field: "
                                + field.getName()
                );
            }
        }

        List<Field> result =
                List.copyOf(fields);

        CACHE.putIfAbsent(type, result);

        return CACHE.get(type);
    }
}