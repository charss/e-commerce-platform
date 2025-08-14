package com.example.payment_svc.dto;

import java.util.List;

public record PaymentTimelineDto(
        Long paymentId,
        String payment_intent_id,
        List<PaymentStatusDto> timeline
) { }
