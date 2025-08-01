package com.example.order_svc.exception;

public class ProductClientException extends RuntimeException {
    public ProductClientException(String message) {
        super(message);
    }
}
