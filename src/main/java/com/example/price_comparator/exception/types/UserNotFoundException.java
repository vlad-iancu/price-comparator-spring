package com.example.price_comparator.exception.types;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("Incorrect combination of username and password");
    }
}
