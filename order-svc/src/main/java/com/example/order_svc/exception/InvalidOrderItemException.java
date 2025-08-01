package com.example.order_svc.exception;

public class InvalidOrderItemException extends RuntimeException {
    public InvalidOrderItemException(String message) {
        super(message);
    }
}
