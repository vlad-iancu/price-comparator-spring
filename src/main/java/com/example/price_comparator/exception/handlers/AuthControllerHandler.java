package com.example.price_comparator.exception.handlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.price_comparator.exception.types.ErrorResponse;
import com.example.price_comparator.exception.types.UserAlreadyExistsException;

@RestControllerAdvice
public class AuthControllerHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        // Handle the exception and return a response
        
        ErrorResponse errorResponse = new ErrorResponse(
            UserAlreadyExistsException.class.getSimpleName(),
            "User already exists"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

}

