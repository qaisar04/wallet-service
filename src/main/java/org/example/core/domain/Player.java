package org.example.core.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.core.domain.types.PlayerRole;

import java.math.BigDecimal;

/**
 * The `Player` class represents a player in the system.
 * It contains information about the player's details, including their unique identifier,
 * full name, password, and current balance.
 */
@Data
@Builder
@Entity
@Table(name = "players", schema = "wallet")
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    /**
     * The unique identifier of the player.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * The username of the player.
     */
    @NotNull
    @Column(name = "username", unique = true)
    private String username;

    /**
     * The player's password for authentication.
     */
    @NotNull
    @Column(name = "password")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private PlayerRole playerRole;

    /**
     * The current balance of the player, represented as a decimal value.
     */
    @Column(name = "balance")
    private BigDecimal balance = BigDecimal.ZERO;

    public Player(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
