package com.example.price_comparator.exception.handlers;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.price_comparator.exception.types.ErrorResponse;
import com.example.price_comparator.exception.types.ProductAlreadyExistsException;
import com.example.price_comparator.exception.types.ProductNotFoundException;
import com.example.price_comparator.exception.types.ShoppingListNotFoundException;
import com.example.price_comparator.exception.types.UnauthorizedShoppingListException;
import com.example.price_comparator.exception.types.ShoppingListEmptyException;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE) // Ensure this handler is processed first
public class ShoppingListControllerHandler {

    @ExceptionHandler(ShoppingListNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleShoppingListNotFoundException(ShoppingListNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(
            ShoppingListNotFoundException.class.getSimpleName(),
            e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ShoppingListEmptyException.class)
    public ResponseEntity<ErrorResponse> handleShoppingListEmptyException(ShoppingListEmptyException e) {
        ErrorResponse errorResponse = new ErrorResponse(
            ShoppingListEmptyException.class.getSimpleName(),
            "Not found"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedShoppingListException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedShoppingListException(UnauthorizedShoppingListException e) {
        // No information about other users' shopping lists should be returned
        System.out.println("Unauthorized access to shopping list (in handler): " + e.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
            ShoppingListNotFoundException.class.getSimpleName(),
            "Not found"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleProductAlreadyExistsException(ProductAlreadyExistsException e) {
        ErrorResponse errorResponse = new ErrorResponse(
            ProductAlreadyExistsException.class.getSimpleName(),
            e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(
            ProductNotFoundException.class.getSimpleName(),
            e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
