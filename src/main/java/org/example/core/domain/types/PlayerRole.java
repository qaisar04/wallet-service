package org.example.core.domain.types;

/**
 * The `PlayerRole` enumeration defines two possible roles for players.
 * Players can have either an "ADMIN" role or a "USER" role, which determines their level of access and privileges within the system.
 */
public enum PlayerRole {
    /**
     * Represents the "ADMIN" role, indicating an administrator with elevated access and privileges.
     */
    ADMIN,

    /**
     * Represents the "USER" role, indicating a regular user with standard access and privileges.
     */
    USER
}
