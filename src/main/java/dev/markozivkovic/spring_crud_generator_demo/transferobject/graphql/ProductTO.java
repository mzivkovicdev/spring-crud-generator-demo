package dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import dev.markozivkovic.spring_crud_generator_demo.myenums.StatusEnum;

public record ProductTO(Long id, @NotNull @Size(max = 10000) String name, @NotNull String price, List<UserTO> users, @NotNull UUID uuid, LocalDate birthDate, StatusEnum status) {

}