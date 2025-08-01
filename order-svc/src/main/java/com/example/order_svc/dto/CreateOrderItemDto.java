package com.example.order_svc.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateOrderItemDto(
        @NotNull(message = "Product ID is required")
        Integer productVarId,
        @NotNull(message = "Quantity is required")
        Integer quantity
) { }
