package com.example.price_comparator.exception.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.price_comparator.exception.types.ErrorResponse;
import com.example.price_comparator.exception.types.IncorrectPasswordException;
import com.example.price_comparator.exception.types.UserAlreadyExistsException;
import com.example.price_comparator.exception.types.UserNotFoundException;

@RestControllerAdvice
public class AuthControllerHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        // Handle the exception and return a response
        
        ErrorResponse errorResponse = new ErrorResponse(
            UserAlreadyExistsException.class.getSimpleName(),
            e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        // Handle the exception and return a response
        
        ErrorResponse errorResponse = new ErrorResponse(
            "IncorrectCredentials",
            e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<ErrorResponse> handleIncorrectPasswordException(IncorrectPasswordException e) {
        // Handle the exception and return a response
        
        ErrorResponse errorResponse = new ErrorResponse(
            "IncorrectCredentials",
            e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }
}

