package com.example.payment_svc.exception;

public class PaymentMethodRequiredException extends RuntimeException {
  public PaymentMethodRequiredException(String message) {
    super(message);
  }
}
