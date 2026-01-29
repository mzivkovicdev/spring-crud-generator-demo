package dev.markozivkovic.spring_crud_generator_demo.exception;

public class InvalidResourceStateException extends RuntimeException {

    public InvalidResourceStateException(final String message) {
        super(message);
    }
    
}