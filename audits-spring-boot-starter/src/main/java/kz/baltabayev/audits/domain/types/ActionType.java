package kz.baltabayev.audits.domain.types;

/**
 * An enumeration representing various types of actions that can be performed within a system or application.
 * Each action type corresponds to a specific operation or behavior.
 */
public enum ActionType {
    /**
     * Represents the action of user registration.
     */
    REGISTRATION,

    /**
     * Represents the action of user authorization.
     */
    AUTHORIZATION,

    /**
     * Represents the action of checking the balance of an account.
     */
    BALANCE_INQUIRY,

    /**
     * Represents a credit transaction, which typically involves adding funds to an account.
     */
    CREDIT_TRANSACTION,

    /**
     * Represents a debit transaction, which typically involves subtracting funds from an account.
     */
    DEBIT_TRANSACTION,

    /**
     * Represents the action of viewing transaction history or logs.
     */
    VIEW_TRANSACTION_HISTORY
}
