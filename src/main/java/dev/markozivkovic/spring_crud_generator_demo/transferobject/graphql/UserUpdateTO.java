package dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql;

import java.util.List;

import dev.markozivkovic.spring_crud_generator_demo.transferobject.graphql.helpers.DetailsTO;

public record UserUpdateTO(String username, String email, String password, DetailsTO details, List<String> roles, List<String> permissions) {

}