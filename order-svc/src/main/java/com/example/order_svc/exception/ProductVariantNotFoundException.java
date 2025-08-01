package com.example.order_svc.exception;

public class ProductVariantNotFoundException extends RuntimeException {
    public ProductVariantNotFoundException(String message) {
        super(message);
    }
}
