package com.example.shopping_cart_svc.exception;

public class InvalidUpsertException extends RuntimeException {
    public InvalidUpsertException(String message) {
        super(message);
    }
}
