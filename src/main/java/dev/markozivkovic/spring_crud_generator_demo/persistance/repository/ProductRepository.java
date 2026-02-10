package dev.markozivkovic.spring_crud_generator_demo.persistance.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import dev.markozivkovic.spring_crud_generator_demo.persistance.entity.ProductModel;

public interface ProductRepository extends JpaRepository<ProductModel, Long> {

    @EntityGraph(value = "Product.withUsers", type = EntityGraph.EntityGraphType.LOAD)
    Optional<ProductModel> findById(final Long id);
    
}