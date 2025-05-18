package com.example.price_comparator.exception.types;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String error;
    private final String message;

    public ErrorResponse(String error, String message) {
        this.error = error;
        this.message = message;
    }

    // Getters and setters (or use Lombok @Getter/@Setter)
}

