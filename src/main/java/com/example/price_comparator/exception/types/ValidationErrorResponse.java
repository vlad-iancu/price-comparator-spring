package com.example.price_comparator.exception.types;

import java.util.Map;

import lombok.Getter;

@Getter
public class ValidationErrorResponse {
    private final String error;
    private final Map<String, String> message;
    private final int status;

    public ValidationErrorResponse(String error, Map<String, String> message, int status) {
        this.error = error;
        this.message = message;
        this.status = status;
    }

    // Getters and setters (or use Lombok @Getter/@Setter)
}