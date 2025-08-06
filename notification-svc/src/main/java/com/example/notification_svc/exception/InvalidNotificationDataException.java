package com.example.notification_svc.exception;

public class InvalidNotificationDataException extends RuntimeException {
    public InvalidNotificationDataException(String message) {
        super(message);
    }
}
