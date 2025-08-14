package com.example.shared.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PaymentRequestDto(
  @NotNull(message = "Order ID is required")
  Long orderId,
  @NotNull(message = "User ID is required")
  UUID userId,
  @NotNull(message = "Amount is required")
  Long amount,
  @NotBlank(message = "Currency is required")
  String currency,
  String payment_method
) {}
