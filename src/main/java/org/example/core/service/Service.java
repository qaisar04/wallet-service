package org.example.core.service;


import java.util.List;
import java.util.Optional;

/**
 * The {@code Service} interface defines a generic set of methods for CRUD (Create, Read, Update, Delete)
 * operations on domain objects. Implementing classes or interfaces will provide specific functionality
 * for the corresponding domain entity.
 *
 * @param <T> The type of the unique identifier (e.g., Integer, Long) for the domain entity.
 * @param <V> The domain entity type that the service manages (e.g., Player, Transaction).
 */
public interface Service<T, V> {
    /**
     * Retrieves an entity by its unique identifier.
     *
     * @param id The unique identifier of the entity to retrieve.
     * @return An {@code Optional} containing the entity if found, or an empty {@code Optional} if not found.
     */
    Optional<V> findById(T id);

    /**
     * Retrieves all entities of the specified type.
     *
     * @return A list of all entities in the data store.
     */
    List<V> findAll();

    /**
     * Saves an entity to the data store.
     *
     * @param v The entity to save.
     * @return The saved entity with any auto-generated fields populated.
     */
    void save(V v);

    /**
     * Updates an existing entity in the data store.
     *
     * @param v The entity to update.
     */
    void update(V v);

    /**
     * Deletes an entity from the data store by its unique identifier.
     *
     * @param id The unique identifier of the entity to delete.
     * @return {@code true} if the entity was successfully deleted, or {@code false} if it was not found.
     */
    boolean delete(T id);

    /**
     * Deletes all entities of the specified type from the data store.
     *
     * @return {@code true} if one or more entities were successfully deleted, or {@code false} if none were found.
     */
    boolean deleteAll();
}
