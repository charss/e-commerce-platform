package com.example.shopping_cart_svc.exception;

public class UserCartNotFoundException extends RuntimeException {
    public UserCartNotFoundException(String message) {
        super(message);
    }
}
