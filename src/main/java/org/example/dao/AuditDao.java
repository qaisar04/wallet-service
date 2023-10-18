package org.example.dao;

import java.util.List;
import java.util.Optional;

/**
 * The `AuditDao` interface defines the common methods for interacting with audit records in a data store.
 *
 * @param <T> The type of the identifier used for audit records.
 * @param <V> The type of the audit record objects.
 */
public interface AuditDao<T, V> {

    /**
     * Find an audit record by its identifier.
     *
     * @param id The identifier of the audit record to find.
     * @return An optional containing the audit record if found, or an empty optional if not found.
     */
    Optional<V> findById(T id);

    /**
     * Retrieve all audit records.
     *
     * @return A list of all audit records.
     */
    List<V> findAll();

    /**
     * Save a new audit record or update an existing one.
     *
     * @param v The audit record to save or update.
     * @return The saved or updated audit record.
     */
    V save(V v);

    /**
     * Update an existing audit record.
     *
     * @param v The audit record to update.
     */
    void update(V v);

    /**
     * Delete an audit record by its identifier.
     *
     * @param id The identifier of the audit record to delete.
     * @return `true` if the deletion was successful, or `false` if no record was found to delete.
     */
    boolean delete(T id);

    /**
     * Delete all audit records.
     *
     * @return `true` if the deletion was successful, or `false` if no records were found to delete.
     */
    boolean deleteAll();
}
