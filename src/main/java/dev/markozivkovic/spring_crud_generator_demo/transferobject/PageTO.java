package dev.markozivkovic.spring_crud_generator_demo.transferobject;

import java.util.List;

public record PageTO<T>(
    int totalPages, long totalElements, int size, int number, List<T> content
) {
    
}
