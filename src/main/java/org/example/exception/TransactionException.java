package org.example.exception;

/**
 * Exception indicating that a player could not be found.
 */
public class TransactionException extends RuntimeException {
    public TransactionException(Throwable cause) {
        super("Exception while processing transaction. Try again", cause);
    }

    /**
     * Constructs a new PlayerNotFoundException with the specified detail message.
     *
     * @param message The detail message describing the exception.
     */
    public TransactionException(String message) {
        super(message);
    }
}
