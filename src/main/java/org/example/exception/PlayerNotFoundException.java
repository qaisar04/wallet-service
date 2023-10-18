package org.example.exception;

/**
 * Exception indicating that a player could not be found.
 */
public class PlayerNotFoundException extends RuntimeException {
    /**
     * Constructs a new PlayerNotFoundException with the specified detail message.
     *
     * @param message The detail message describing the exception.
     */
    public PlayerNotFoundException(String message) {
        super(message);
    }
}
