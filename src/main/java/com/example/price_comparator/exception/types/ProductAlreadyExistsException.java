package com.example.price_comparator.exception.types;

public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException(String productName) {
        super("Product with name '" + productName + "' already exists.");
    }
}
