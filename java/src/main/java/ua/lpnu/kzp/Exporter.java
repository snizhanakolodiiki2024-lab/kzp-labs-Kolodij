package ua.lpnu.kzp;

import java.nio.file.Path;
import java.util.List;

/**
 * Defines a common contract for exporting objects to a file.
 *
 * @param <T> exported object type
 */
public interface Exporter<T> {

    /**
     * Exports records to the specified file.
     *
     * @param path output file path
     * @param type exported object class
     * @param records records to export
     * @throws DataStorageException if export fails
     */
    void export(
            Path path,
            Class<T> type,
            List<? extends T> records)
            throws DataStorageException;
}