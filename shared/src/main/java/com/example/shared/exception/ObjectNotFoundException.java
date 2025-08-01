package com.example.shared.exception;

public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String objectName, Object id) {
        super(objectName + " with ID " + id + " not found.");
    }
}
