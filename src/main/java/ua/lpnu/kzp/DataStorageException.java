package ua.lpnu.kzp;

/**
 * Signals an error while storing, exporting or restoring application data.
 */
public class DataStorageException extends Exception {

    /**
     * Creates an exception with the specified message.
     *
     * @param message error description
     */
    public DataStorageException(String message) {
        super(message);
    }

    /**
     * Creates an exception with the specified message and original cause.
     *
     * @param message error description
     * @param cause original cause of the error
     */
    public DataStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}