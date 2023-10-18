package org.example.dao;

import java.util.List;
import java.util.Optional;

/**
 * The `TransactionDao` interface defines common methods for interacting with transaction entities in a data store.
 *
 * @param <T> The type of identifier used for transaction entities.
 * @param <V> The type of transaction entity objects.
 */
public interface TransactionDao<T, V> {
    /**
     * Find a transaction entity by its identifier.
     *
     * @param id The identifier of the transaction entity to find.
     * @return An optional containing the transaction entity if found, or an empty optional if not found.
     */
    Optional<V> findById(T id);

    /**
     * Retrieve all transaction entities.
     *
     * @return A list of all transaction entities.
     */
    List<V> findAll();

    /**
     * Save a new transaction entity or update an existing one.
     *
     * @param v The transaction entity to save or update.
     * @return The saved or updated transaction entity.
     */
    V save(V v);

    /**
     * Update an existing transaction entity.
     *
     * @param v The transaction entity to update.
     */
    void update(V v);

    /**
     * Delete a transaction entity by its identifier.
     *
     * @param id The identifier of the transaction entity to delete.
     * @return `true` if the deletion was successful, or `false` if no entity was found to delete.
     */
    boolean delete(T id);

    /**
     * Delete all transaction entities.
     *
     * @return `true` if the deletion was successful, or `false` if no entities were found to delete.
     */
    boolean deleteAll();
}
