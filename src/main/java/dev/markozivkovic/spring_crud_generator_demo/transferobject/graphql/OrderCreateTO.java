package dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record OrderCreateTO(Long productId, @NotNull Integer quantity, List<Long> usersIds) {

}