package com.example.order_svc.dto;

import com.example.order_svc.entity.Order;
import com.example.shared.enums.OrderStatus;
import com.example.shared.util.MoneyUtil;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record OrderResponseDto(
        Long id,
        UUID userId,
        List<OrderItemResponseDto> items,
        String totalAmount,
        OrderStatus status,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        OffsetDateTime timestamp
) {
        public static OrderResponseDto from (Order order) {
                return new OrderResponseDto(
                        order.getId(),
                        order.getUserId(),
                        order.getItems().stream().map(OrderItemResponseDto::from).toList(),
                        MoneyUtil.format(order.getTotalAmountMinor(), order.getCurrency()),
                        order.getStatus(),
                        order.getOrderDate()
                );
        }
}
