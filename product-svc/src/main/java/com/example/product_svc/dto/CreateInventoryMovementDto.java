package com.example.product_svc.dto;

import com.example.product_svc.common.MovementType;
import jakarta.validation.constraints.NotNull;

public record CreateInventoryMovementDto(
        @NotNull(message = "Variant Id is required")
        Integer variantId,
        @NotNull(message = "Quantity is required")
        Integer quantity,
        @NotNull(message = "Movement Type is required")
        MovementType movementType
) { }
