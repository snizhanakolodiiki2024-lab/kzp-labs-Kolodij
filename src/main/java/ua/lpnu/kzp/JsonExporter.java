package ua.lpnu.kzp;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Exports objects with CsvColumn annotations to UTF-8 JSON.
 *
 * @param <T> exported object type
 */
public final class JsonExporter<T> implements Exporter<T> {

    /**
     * Creates a JSON exporter.
     */
    public JsonExporter() {
    }

    /**
     * Exports annotated fields to a JSON file.
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

        Objects.requireNonNull(path, "path must not be null");
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(records, "records must not be null");

        List<Field> fields =
                ExportFieldCache.fieldsFor(type);

        if (fields.isEmpty()) {
            throw new DataStorageException(
                    "class has no export fields"
            );
        }

        StringBuilder json = new StringBuilder();
        json.append("[")
                .append(System.lineSeparator());

        for (int recordIndex = 0;
                recordIndex < records.size();
                recordIndex++) {

            T record = Objects.requireNonNull(
                    records.get(recordIndex),
                    "record must not be null"
            );

            json.append("  {")
                    .append(System.lineSeparator());

            for (int fieldIndex = 0;
                    fieldIndex < fields.size();
                    fieldIndex++) {

                Field field = fields.get(fieldIndex);

                String name = field
                        .getAnnotation(CsvColumn.class)
                        .value();

                Object value = readField(field, record);

                json.append("    \"")
                        .append(escape(name))
                        .append("\": ")
                        .append(toJsonValue(value));

                if (fieldIndex < fields.size() - 1) {
                    json.append(",");
                }

                json.append(System.lineSeparator());
            }

            json.append("  }");

            if (recordIndex < records.size() - 1) {
                json.append(",");
            }

            json.append(System.lineSeparator());
        }

        json.append("]")
                .append(System.lineSeparator());

        try {
            Files.writeString(
                    path,
                    json.toString(),
                    StandardCharsets.UTF_8
            );
        } catch (IOException exception) {
            throw new DataStorageException(
                    "failed to write JSON file",
                    exception
            );
        }
    }

    private static Object readField(
            Field field,
            Object record)
            throws DataStorageException {

        try {
            return field.get(record);
        } catch (IllegalAccessException exception) {
            throw new DataStorageException(
                    "cannot read field: "
                            + field.getName(),
                    exception
            );
        }
    }

    private static String toJsonValue(Object value) {
        if (value == null) {
            return "null";
        }

        if (value instanceof Number
                || value instanceof Boolean) {
            return value.toString();
        }

        return "\"" + escape(value.toString()) + "\"";
    }

    private static String escape(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\r", "\\r")
                .replace("\n", "\\n")
                .replace("\t", "\\t");
    }
}