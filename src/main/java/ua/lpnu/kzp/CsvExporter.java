package ua.lpnu.kzp;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Exports objects with CsvColumn annotations to UTF-8 CSV.
 *
 * @param <T> exported object type
 */
public final class CsvExporter<T> implements Exporter<T> {

    /**
     * Creates a CSV exporter.
     */
    public CsvExporter() {
    }

    /**
     * Exports records to a CSV file.
     *
     * @param path output file path
     * @param type exported object class
     * @param records records to export
     * @throws DataStorageException if export fails
     */
    @Override
    public void export(
            Path path,
            Class<T> type,
            List<? extends T> records)
            throws DataStorageException {

        write(path, type, records);
    }

    /**
     * Writes annotated fields of the specified records to a CSV file.
     *
     * @param <R> record type
     * @param path output file path
     * @param type record class
     * @param records records to export
     * @throws DataStorageException if export fails
     */
    public static <R> void write(
            Path path,
            Class<R> type,
            List<? extends R> records)
            throws DataStorageException {

        Objects.requireNonNull(
                path,
                "path must not be null"
        );

        Objects.requireNonNull(
                type,
                "type must not be null"
        );

        Objects.requireNonNull(
                records,
                "records must not be null"
        );

        List<Field> fields =
                ExportFieldCache.fieldsFor(type);

        if (fields.isEmpty()) {
            throw new DataStorageException(
                    "class has no CSV fields"
            );
        }

        String header = fields.stream()
                .map(field ->
                        field.getAnnotation(
                                CsvColumn.class
                        ).value()
                )
                .map(CsvExporter::escape)
                .collect(Collectors.joining(","));

        StringBuilder csv = new StringBuilder(header)
                .append(System.lineSeparator());

        for (R record : records) {
            Objects.requireNonNull(
                    record,
                    "record must not be null"
            );

            List<String> values = new ArrayList<>();

            for (Field field : fields) {
                values.add(
                        escape(
                                readField(
                                        field,
                                        record
                                )
                        )
                );
            }

            csv.append(String.join(",", values))
                    .append(System.lineSeparator());
        }

        try {
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

    private static String readField(
            Field field,
            Object record)
            throws DataStorageException {

        try {
            Object value = field.get(record);

            return value == null
                    ? ""
                    : value.toString();
        } catch (IllegalAccessException exception) {
            throw new DataStorageException(
                    "cannot read field: "
                            + field.getName(),
                    exception
            );
        }
    }

    private static String escape(String value) {
        boolean requiresQuotes =
                value.contains(",")
                        || value.contains("\"")
                        || value.contains("\n")
                        || value.contains("\r");

        String escaped = value.replace(
                "\"",
                "\"\""
        );

        if (requiresQuotes) {
            return "\"" + escaped + "\"";
        }

        return escaped;
    }
}