package org.example.dao;

import java.util.List;
import java.util.Optional;

/**
 * The `PlayerDao` interface defines common methods for interacting with player entities in a data store.
 *
 * @param <ID>     The type of identifier used for player entities.
 * @param <Entity> The type of player entity objects.
 */
public interface PlayerDao<ID, Entity> {

    /**
     * Find a player entity by its identifier.
     *
     * @param id The identifier of the player entity to find.
     * @return An optional containing the player entity if found, or an empty optional if not found.
     */
    Optional<Entity> findById(ID id);

    /**
     * Retrieve all player entities.
     *
     * @return A list of all player entities.
     */
    List<Entity> findAll();

    /**
     * Save a new player entity or update an existing one.
     *
     * @param entity The player entity to save or update.
     * @return The saved or updated player entity.
     */
    Entity save(Entity entity);

    /**
     * Update an existing player entity.
     *
     * @param entity The player entity to update.
     */
    void update(Entity entity);

    /**
     * Delete a player entity by its identifier.
     *
     * @param id The identifier of the player entity to delete.
     * @return `true` if the deletion was successful, or `false` if no entity was found to delete.
     */
    boolean delete(ID id);

    /**
     * Delete all player entities.
     *
     * @return `true` if the deletion was successful, or `false` if no entities were found to delete.
     */
    boolean deleteAll();

}
