package dev.markozivkovic.spring_crud_generator_demo.transferobject.rest;

import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderTO(Long orderId, @NotNull ProductTO product, @NotNull @NotNull @Min(1) @Max(100) Integer quantity, @NotNull List<UserTO> users) {

}