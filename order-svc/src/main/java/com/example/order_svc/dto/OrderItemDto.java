package com.example.order_svc.dto;

import com.example.order_svc.entity.OrderItem;

public record OrderItemDto(
        Long id,
        Long orderId,
        Integer productId,
        String sku,
        Integer quantity,
        Double pricePerUnit,
        Double subtotal
) {
    public static OrderItemDto from (OrderItem item) {
        return new OrderItemDto(
                item.getId(),
                item.getOrder().getId(),
                item.getProductVariantId(),
                item.getSku(),
                item.getQuantity(),
                item.getPricePerUnit(),
                item.getSubtotal()
        );
    }
}
