package dev.markozivkovic.spring_crud_generator_demo.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.OrderTable;

public interface OrderRepository extends JpaRepository<OrderTable, Long> {

}