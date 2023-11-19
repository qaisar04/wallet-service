package org.example.core.service;

import org.example.core.domain.Player;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * The interface Player service.
 */
public interface PlayerService {

    /**
     * Get player's balance.
     *
     * @param id the id
     * @return the player balance
     */
    BigDecimal getPlayerBalance(Long id);

    /**
     * Update balance boolean.
     *
     * @param id      the id
     * @param balance the balance
     */
    void updateBalance(Long id, BigDecimal balance);

    /**
     * Get player by login.
     *
     * @param login the login
     * @return the player entity
     */
    Player findByUsername(String username);

}
