package com.example.product_svc.exception;

public class InvalidMovementTypeException extends RuntimeException {
    public InvalidMovementTypeException(String message) {
        super(message);
    }
}
