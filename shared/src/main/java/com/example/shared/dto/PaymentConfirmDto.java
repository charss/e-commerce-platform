package com.example.shared.dto;

import jakarta.validation.constraints.NotBlank;

public record PaymentConfirmDto(
  @NotBlank(message = "Payment Intent ID is required")
  String paymentIntentId,
  String paymentMethodId
) {} 
