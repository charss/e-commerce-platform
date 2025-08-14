package com.example.payment_svc.dto;

import com.stripe.model.PaymentIntent;

public record PaymentIntentDto(
        String id,
        String status,
        Long amount,
        String currency,
        Long createdAt
) {
    public static PaymentIntentDto from (PaymentIntent intent) {
        return new PaymentIntentDto(
                intent.getId(),
                intent.getStatus(),
                intent.getAmount(),
                intent.getCurrency(),
                intent.getCreated()
        );
    }
}
