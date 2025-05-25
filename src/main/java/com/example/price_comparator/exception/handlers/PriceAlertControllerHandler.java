
package com.example.price_comparator.exception.handlers;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.price_comparator.exception.types.ErrorResponse;
import com.example.price_comparator.exception.types.PriceAlertConflictException;
import com.example.price_comparator.exception.types.PriceAlertNotFoundException;
import com.example.price_comparator.exception.types.UnauthorizedPriceAlertException;

@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class PriceAlertControllerHandler {

    @ExceptionHandler(PriceAlertNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePriceAlertNotFoundException(PriceAlertNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse(
            PriceAlertNotFoundException.class.getSimpleName(),
            e.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedPriceAlertException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedPriceAlertException(UnauthorizedPriceAlertException e) {
        ErrorResponse errorResponse = new ErrorResponse(
            PriceAlertNotFoundException.class.getSimpleName(),
            "Not Found"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PriceAlertConflictException.class)
    public ResponseEntity<ErrorResponse> handlePriceAlertConflictException(PriceAlertConflictException e) {
        ErrorResponse errorResponse = new ErrorResponse(
            PriceAlertConflictException.class.getSimpleName(),
            "Not Found"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}
