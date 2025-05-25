package com.example.price_comparator.exception.handlers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.price_comparator.exception.types.ErrorResponse;
import com.example.price_comparator.exception.types.ValidationErrorResponse;

import jakarta.validation.UnexpectedTypeException;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE)
public class GlobalControllerHandler {

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
                ErrorResponse error = new ErrorResponse(
                                "Internal Server Error",
                                "An internal server error has occurred.");
                System.err.println("An error occurred: " + ex.getMessage());
                System.out.println("Actual type: " + ex.getClass().getName());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);

        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ValidationErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getFieldErrors()
                                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

                ValidationErrorResponse response = new ValidationErrorResponse(
                                "ValidationError",
                                errors,
                                HttpStatus.BAD_REQUEST.value());

                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
        public ResponseEntity<ErrorResponse> handleJsonParseError(
                        org.springframework.http.converter.HttpMessageNotReadableException ex) {
                ErrorResponse error = new ErrorResponse(
                                "Bad Request",
                                "Malformed JSON request.");
                System.err.println("JSON parse error: " + ex.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

        @ExceptionHandler(UnexpectedTypeException.class)
        public ResponseEntity<ErrorResponse> handleUnexpectedTypeException(UnexpectedTypeException ex) {
                ErrorResponse error = new ErrorResponse(
                                "Unexpected Type",
                                ex.getMessage());
                System.err.println("Unexpected type error: " + ex.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }

}
