package com.example.payment_svc.dto;

import com.example.payment_svc.entity.Payment;
import com.stripe.model.PaymentIntent;

public record PaymentWithIntentDto(
        Payment payment,
        PaymentIntent paymentIntent
) {
}
