package com.example.shared.enums;


import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum NotificationType {
    USER_REGISTERED,
    ORDER_CONFIRMED,
    ORDER_SHIPPED,
    ORDER_DELIVERED,
    PAYMENT_SUCCESS,
    PAYMENT_FAILED,
    PASSWORD_RESET;
}
