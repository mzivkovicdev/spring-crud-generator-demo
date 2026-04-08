package dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import dev.markozivkovic.spring_crud_generator_demo.myenums.StatusEnum;
import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.helpers.ProductDetailsTO;

public record ProductTO(Long id, @NotNull @NotNull @Size(min = 10, max = 100) @NotBlank String name, @NotNull @NotNull @Min(1) @Max(100) Integer price, @NotNull List<UserTO> users, @NotNull @NotNull UUID uuid, LocalDate releaseDate, List<ProductDetailsTO> details, StatusEnum status) {

}