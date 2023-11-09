package kz.baltabayev.audits.domain;

import kz.baltabayev.audits.domain.types.ActionType;
import kz.baltabayev.audits.domain.types.AuditType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The `Audit` class represents an audit record in the system.
 * It contains information about various audit details, including the audit type,
 * action type, and the associated player's full name.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Audit {
    /**
     * The unique identifier of the audit record.
     */
    private Integer id;

    /**
     * The type of audit, such as success or failure.
     */
    private AuditType auditType;

    /**
     * The type of action that was audited.
     */
    private ActionType actionType;

    /**
     * The full name of the player associated with the audit.
     */
    private String playerFullName;
}