package dev.markozivkovic.spring_crud_generator_demo.transferobject.rest;

import java.util.List;

import dev.markozivkovic.spring_crud_generator_demo.transferobject.rest.helpers.DetailsTO;

public record UserTO(Long userId, String username, String email, String password, DetailsTO details, List<String> roles, List<String> permissions) {

}