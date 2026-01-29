package dev.markozivkovic.spring_crud_generator_demo.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

}