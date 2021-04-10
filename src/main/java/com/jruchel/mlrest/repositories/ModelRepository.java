package com.jruchel.mlrest.repositories;

import com.jruchel.mlrest.models.Model;
import com.jruchel.mlrest.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface ModelRepository extends JpaRepository<Model, Integer> {
    @Query(nativeQuery = true, value = "select * from model where owner_id = ?1 and name = ?2 limit 1")
    Model findByName(long userId, String modelName);
//    Model findByOwnerAndName(User user, String modelName);

}