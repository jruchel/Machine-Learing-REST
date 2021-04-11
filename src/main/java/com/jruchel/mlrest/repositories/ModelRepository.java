package com.jruchel.mlrest.repositories;

import com.jruchel.mlrest.models.Model;
import com.jruchel.mlrest.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface ModelRepository extends JpaRepository<Model, Integer> {
    Model findByOwnerAndName(User owner, String name);
}