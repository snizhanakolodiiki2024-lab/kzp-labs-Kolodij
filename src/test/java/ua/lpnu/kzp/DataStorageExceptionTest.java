package ua.lpnu.kzp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.IOException;

import org.junit.jupiter.api.Test;

/**
 * Tests DataStorageException.
 */
class DataStorageExceptionTest {

    @Test
    void storesMessage() {
        DataStorageException exception =
                new DataStorageException("storage error");

        assertEquals("storage error", exception.getMessage());
    }

    @Test
    void storesOriginalCause() {
        IOException cause = new IOException("file error");

        DataStorageException exception =
                new DataStorageException("storage error", cause);

        assertEquals("storage error", exception.getMessage());
        assertSame(cause, exception.getCause());
    }
}