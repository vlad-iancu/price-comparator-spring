package com.example.price_comparator.exception.types;

public class IncorrectPasswordException extends RuntimeException {
    public IncorrectPasswordException(String username) {
        super("Incorrect combination of username and password");
    }
    
}
