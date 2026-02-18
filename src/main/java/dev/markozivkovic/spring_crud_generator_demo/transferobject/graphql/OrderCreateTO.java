package dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql;

import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderCreateTO(Long productId, @NotNull @NotNull @Min(1) @Max(100) Integer quantity, List<Long> usersIds) {

}