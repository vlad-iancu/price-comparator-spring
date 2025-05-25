package com.example.price_comparator.exception.types;

public class ShoppingListEmptyException extends RuntimeException {
    public ShoppingListEmptyException(String message) {
        super(message);
    }
}
