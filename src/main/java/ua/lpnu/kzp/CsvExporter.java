package ua.lpnu.kzp;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Exports objects with CsvColumn annotations to UTF-8 CSV.
 */
public final class CsvExporter {

    private CsvExporter() {
    }

    /**
     * Writes annotated fields of the specified records to a CSV file.
     *
     * @param <T> record type
     * @param path output file path
     * @param type record class
     * @param records records to export
     * @throws DataStorageException if export fails
     */
    public static <T> void write(
            Path path,
            Class<T> type,
            List<? extends T> records)
            throws DataStorageException {

        Objects.requireNonNull(path, "path must not be null");
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(records, "records must not be null");

        List<Field> fields = annotatedFields(type);

        if (fields.isEmpty()) {
            throw new DataStorageException(
                    "class has no CSV fields"
            );
        }

        String header = fields.stream()
                .map(field -> field.getAnnotation(CsvColumn.class).value())
                .collect(Collectors.joining(","));

        StringBuilder csv = new StringBuilder(header)
                .append(System.lineSeparator());

        try {
            for (T record : records) {
                Objects.requireNonNull(
                        record,
                        "record must not be null"
                );

                String row = fields.stream()
                        .map(field -> readField(field, record))
                        .collect(Collectors.joining(","));

                csv.append(row)
                        .append(System.lineSeparator());
            }

            Files.writeString(
                    path,
                    csv.toString(),
                    StandardCharsets.UTF_8
            );
        } catch (IOException exception) {
            throw new DataStorageException(
                    "failed to write CSV file",
                    exception
            );
        }
    }

    private static List<Field> annotatedFields(Class<?> type)
            throws DataStorageException {

        List<Field> fields = Arrays.stream(type.getDeclaredFields())
                .filter(field ->
                        field.isAnnotationPresent(CsvColumn.class))
                .sorted(Comparator.comparing(Field::getName))
                .toList();

        for (Field field : fields) {
            if (!field.trySetAccessible()) {
                throw new DataStorageException(
                        "cannot access field: " + field.getName()
                );
            }
        }

        return fields;
    }

    private static String readField(Field field, Object record) {
        try {
            Object value = field.get(record);
            return value == null ? "" : value.toString();
        } catch (IllegalAccessException exception) {
            throw new IllegalStateException(
                    "cannot read field: " + field.getName(),
                    exception
            );
        }
    }
}