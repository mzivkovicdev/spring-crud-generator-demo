package dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql;

import java.util.List;

import jakarta.validation.constraints.NotNull;

public record OrderTO(Long orderId, ProductTO product, @NotNull Integer quantity, List<UserTO> users) {

}