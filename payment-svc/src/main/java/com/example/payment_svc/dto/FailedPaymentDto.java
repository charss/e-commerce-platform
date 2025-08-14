package com.example.payment_svc.dto;

public record FailedPaymentDto(
        String paymentIntentId,
        String status,
        String declineCode,
        Long chargedAt
) { }
