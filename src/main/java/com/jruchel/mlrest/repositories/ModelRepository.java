package com.jruchel.mlrest.repositories;

import com.jruchel.mlrest.models.Model;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface ModelRepository extends CrudRepository<Model, Integer> {
    @Query(nativeQuery = true, value = "select * from model where owner_id = ?1 and name = ?2 limit 1")
    Model findByName(int userId, String modelName);
}