package com.example.shopping_cart_svc.exception;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String objectName, Object id) {
        super(objectName + " with ID " + id + " not found");
    }
}
