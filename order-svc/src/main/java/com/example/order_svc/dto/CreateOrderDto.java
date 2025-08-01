package com.example.order_svc.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.UUID;

public record CreateOrderDto (
        @NotNull (message = "User ID is required")
        UUID userId,
        @NotNull (message = "Items are required")
        List<CreateOrderItemDto> items
) { }
