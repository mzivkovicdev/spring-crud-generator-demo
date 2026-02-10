package dev.markozivkovic.spring_crud_generator_demo.persistance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @EntityGraph(value = "User.withRolesPermissions", type = EntityGraph.EntityGraphType.LOAD)
    Optional<UserEntity> findById(final Long userId);
    
}