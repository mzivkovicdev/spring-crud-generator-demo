package dev.markozivkovic.spring_crud_generator_demo.persistance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.OrderTable;

public interface OrderRepository extends JpaRepository<OrderTable, Long> {

    @EntityGraph(value = "Order.withUsers", type = EntityGraph.EntityGraphType.LOAD)
    Optional<OrderTable> findById(final Long orderId);
    
}