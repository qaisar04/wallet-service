package kz.baltabayev.audits.domain.types;

/**
 * The `AuditType` enumeration defines two possible types of audit record results.
 * These types are used to indicate the result of an action in audit records.
 */
public enum AuditType {
    /**
     * Represents a successful result in an audit record.
     */
    SUCCESS,

    /**
     * Represents a failed result in an audit record.
     */
    FAIL
}
