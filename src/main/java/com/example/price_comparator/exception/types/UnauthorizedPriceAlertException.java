package com.example.price_comparator.exception.types;

public class UnauthorizedPriceAlertException extends RuntimeException {
    public UnauthorizedPriceAlertException(String message) {
        super(message);
    }
}
