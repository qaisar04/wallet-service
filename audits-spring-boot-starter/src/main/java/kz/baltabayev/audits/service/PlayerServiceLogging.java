package kz.baltabayev.audits.service;

import java.math.BigDecimal;

/**
 * The interface Player service.
 */
public interface PlayerServiceLogging<EntityClass> {

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
    EntityClass findByUsername(String username);

}
