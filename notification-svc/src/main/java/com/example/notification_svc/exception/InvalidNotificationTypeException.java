package com.example.notification_svc.exception;

public class InvalidNotificationTypeException extends RuntimeException {
    public InvalidNotificationTypeException(String message) {
        super(message);
    }
}
