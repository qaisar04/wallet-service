package kz.baltabayev.audits.domain;

import kz.baltabayev.audits.domain.types.ActionType;
import kz.baltabayev.audits.domain.types.AuditType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuditDTO {
    /**
     * The full name of the player associated with the audit.
     */
    private String playerFullName;
    /**
     * The type of audit, such as success or failure.
     */
    private AuditType auditType;
    /**
     * The type of action that was audited.
     */
    private ActionType actionType;
}
