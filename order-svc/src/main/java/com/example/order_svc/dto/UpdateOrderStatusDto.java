package com.example.order_svc.dto;

import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusDto(
        @NotNull(message = "ID is required")
        Long id
) { }
