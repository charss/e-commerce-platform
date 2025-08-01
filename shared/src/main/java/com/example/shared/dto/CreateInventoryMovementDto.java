package com.example.shared.dto;

import com.example.shared.enums.MovementType;
import com.example.shared.enums.SourceType;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;

public record CreateInventoryMovementDto(
        @NotNull(message = "Variant Id is required")
        Integer variantId,
        @NotNull(message = "Quantity is required")
        Integer quantity,
        @NotNull(message = "Movement Type is required")
        MovementType movementType,
        @NotNull(message = "Source Type is required")
        SourceType sourceType,
        Optional<String> sourceId
) { }
