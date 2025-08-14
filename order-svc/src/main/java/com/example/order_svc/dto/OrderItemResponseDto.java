package com.example.order_svc.dto;

import com.example.order_svc.entity.OrderItem;
import com.example.shared.util.MoneyUtil;

public record OrderItemResponseDto(
        Long id,
        Long orderId,
        Integer productId,
        String sku,
        Integer quantity,
        String unitPrice,
        String subtotal
) {
    public static OrderItemResponseDto from (OrderItem item) {
        return new OrderItemResponseDto(
                item.getId(),
                item.getOrder().getId(),
                item.getProductVariantId(),
                item.getSku(),
                item.getQuantity(),
                MoneyUtil.format(item.getUnitPriceMinor(), item.getCurrency()),
                MoneyUtil.format(item.getSubtotalMinor(), item.getCurrency())
        );
    }
}
