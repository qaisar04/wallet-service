package org.example.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<ID, Entity> {
    Optional<Entity> findById(ID id);

    List<Entity> findAll();

    Entity save(Entity entity);

    void update(Entity entity);

    boolean delete(ID id);

    boolean deleteAll();
}
