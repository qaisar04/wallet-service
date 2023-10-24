package org.example.core.domain.types;

/**
 * The `TransactionType` enumeration defines two possible types of financial transactions.
 * These types are used to specify the nature of a transaction, such as "DEBIT" or "CREDIT."
 */
public enum TransactionType {
    /**
     * Represents a "DEBIT" transaction, indicating a deduction or withdrawal of funds from an account.
     */
    DEBIT,

    /**
     * Represents a "CREDIT" transaction, indicating an addition or deposit of funds into an account.
     */
    CREDIT
}
