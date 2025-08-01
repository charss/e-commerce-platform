package com.example.product_svc.dto;

import com.example.product_svc.entity.InventoryMovement;
import com.example.shared.enums.MovementType;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;

public record InventoryMovementDto(
        Integer id,
        String sku,
        Integer quantity,
        MovementType movementType,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        OffsetDateTime timestamp
) {
        public static InventoryMovementDto from(InventoryMovement movement) {
                return new InventoryMovementDto(
                        movement.getId(),
                        movement.getProductVariant().getSku(),
                        movement.getQuantity(),
                        movement.getMovementType(),
                        movement.getTimestamp()
                );
        }
}
