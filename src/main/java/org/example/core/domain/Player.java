package org.example.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * The `Player` class represents a player in the system.
 * It contains information about the player's details, including their unique identifier,
 * full name, password, and current balance.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    /**
     * The unique identifier of the player.
     */
    private Integer id;

    /**
     * The username of the player.
     */
    private String username;

    /**
     * The player's password for authentication.
     */
    private String password;

    /**
     * The current balance of the player, represented as a decimal value.
     */
    private BigDecimal balance = BigDecimal.ZERO;
}
