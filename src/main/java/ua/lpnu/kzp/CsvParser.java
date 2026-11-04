package ua.lpnu.kzp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Parses CSV data including quoted and multiline fields.
 */
public final class CsvParser {

    private CsvParser() {
    }

    /**
     * Reads and parses a UTF-8 CSV file.
     *
     * @param path CSV file path
     * @return parsed rows and fields
     * @throws DataStorageException if the file cannot be read
     *         or contains malformed CSV
     */
    public static List<List<String>> read(Path path)
            throws DataStorageException {

        Objects.requireNonNull(path, "path must not be null");

        try {
            String csv = Files.readString(
                    path,
                    StandardCharsets.UTF_8
            );

            return parse(csv);
        } catch (IOException exception) {
            throw new DataStorageException(
                    "failed to read CSV file",
                    exception
            );
        }
    }

    /**
     * Parses CSV text.
     *
     * @param csv CSV text
     * @return parsed rows and fields
     * @throws DataStorageException if CSV syntax is invalid
     */
    public static List<List<String>> parse(String csv)
            throws DataStorageException {

        Objects.requireNonNull(csv, "csv must not be null");

        if (csv.isEmpty()) {
            return List.of();
        }

        List<List<String>> rows = new ArrayList<>();
        List<String> row = new ArrayList<>();
        StringBuilder field = new StringBuilder();

        boolean inQuotes = false;
        boolean closedQuote = false;

        for (int index = 0; index < csv.length(); index++) {
            char current = csv.charAt(index);

            if (inQuotes) {
                if (current == '"') {
                    if (index + 1 < csv.length()
                            && csv.charAt(index + 1) == '"') {

                        field.append('"');
                        index++;
                    } else {
                        inQuotes = false;
                        closedQuote = true;
                    }
                } else {
                    field.append(current);
                }

                continue;
            }

            if (closedQuote) {
                if (current == ',') {
                    row.add(field.toString());
                    field.setLength(0);
                    closedQuote = false;
                    continue;
                }

                if (current == '\n' || current == '\r') {
                    row.add(field.toString());
                    rows.add(List.copyOf(row));

                    row.clear();
                    field.setLength(0);
                    closedQuote = false;

                    if (current == '\r'
                            && index + 1 < csv.length()
                            && csv.charAt(index + 1) == '\n') {
                        index++;
                    }

                    continue;
                }

                throw new DataStorageException(
                        "unexpected character after closing quote"
                );
            }

            if (current == '"') {
                if (!field.isEmpty()) {
                    throw new DataStorageException(
                            "unexpected quote in unquoted field"
                    );
                }

                inQuotes = true;
                continue;
            }

            if (current == ',') {
                row.add(field.toString());
                field.setLength(0);
                continue;
            }

            if (current == '\n' || current == '\r') {
                row.add(field.toString());
                rows.add(List.copyOf(row));

                row.clear();
                field.setLength(0);

                if (current == '\r'
                        && index + 1 < csv.length()
                        && csv.charAt(index + 1) == '\n') {
                    index++;
                }

                continue;
            }

            field.append(current);
        }

        if (inQuotes) {
            throw new DataStorageException(
                    "unclosed quoted field"
            );
        }

        if (closedQuote || !row.isEmpty() || !field.isEmpty()) {
            row.add(field.toString());
            rows.add(List.copyOf(row));
        }

        return List.copyOf(rows);
    }
}