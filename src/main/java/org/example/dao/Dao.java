package org.example.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<Id, Entity> {
    Optional<Entity> findById(Id id);

    List<Entity> findAll();

    Entity save(Entity entity);

    void update(Entity entity);

    boolean delete(Id id);

    boolean deleteAll();
}
