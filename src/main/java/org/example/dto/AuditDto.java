package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.core.domain.types.ActionType;
import org.example.core.domain.types.AuditType;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditDto {

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
