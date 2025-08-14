package com.example.payment_svc.exception;

public class CardDeclinedException extends Exception {
    public CardDeclinedException(String message) {
        super(message);
    }
}
