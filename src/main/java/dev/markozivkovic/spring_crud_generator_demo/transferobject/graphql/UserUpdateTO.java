package dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.helpers.DetailsTO;

public record UserUpdateTO(String username, @NotNull @Email String email, @NotNull @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$") String password, DetailsTO details, List<String> roles, @NotEmpty List<String> permissions) {

}