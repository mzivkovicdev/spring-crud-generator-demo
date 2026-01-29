package dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql;

import jakarta.validation.constraints.NotNull;

public record OrderUpdateTO(@NotNull Integer quantity) {

}