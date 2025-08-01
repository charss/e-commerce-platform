package com.example.product_svc.exception;

public class InvalidMovementSourceException extends RuntimeException {
    public InvalidMovementSourceException(String message) {
        super(message);
    }
}
