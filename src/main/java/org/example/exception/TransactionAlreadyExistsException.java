package org.example.exception;

/**
 * The type Transaction already exists exception.
 */
public class TransactionAlreadyExistsException extends RuntimeException {
    /**
     * Instantiates a new Transaction already exists exception.
     *
     * @param message the message
     */
    public TransactionAlreadyExistsException(String message) {
        super(message);
    }
}
