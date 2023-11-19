package org.example.core.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.core.domain.types.ActionType;
import org.example.core.domain.types.AuditType;

/**
 * The `Audit` class represents an audit record in the system.
 * It contains information about various audit details, including the audit type,
 * action type, and the associated player's full name.
 */
@Data
@Builder
@Entity
@Table(name = "audits", schema = "wallet")
@AllArgsConstructor
@NoArgsConstructor
public class Audit {

    /**
     * The unique identifier of the audit record.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * The type of audit, such as success or failure.
     */
    @Column(name = "audit_type")
    @Enumerated(EnumType.STRING)
    private AuditType auditType;

    /**
     * The type of action that was audited.
     */
    @Column(name = "action_type")
    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    /**
     * The full name of the player associated with the audit.
     */
    @Column(name = "player_username")
    private String playerFullName;
}
