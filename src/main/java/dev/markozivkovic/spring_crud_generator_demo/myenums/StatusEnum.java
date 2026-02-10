package dev.markozivkovic.spring_crud_generator_demo.myenums;

public enum StatusEnum {
    
    ACTIVE("ACTIVE"), 
    INACTIVE("INACTIVE");

    private final String value;

    StatusEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
