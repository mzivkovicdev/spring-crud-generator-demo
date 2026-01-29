package dev.markozivkovic.spring_crud_generator_demo.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(final String message) {
        super(message);
    }
    
}