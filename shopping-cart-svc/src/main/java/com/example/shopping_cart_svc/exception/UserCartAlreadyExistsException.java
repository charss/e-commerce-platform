package com.example.shopping_cart_svc.exception;

public class UserCartAlreadyExistsException extends RuntimeException {
    public UserCartAlreadyExistsException(String message) {
        super(message);
    }
}
