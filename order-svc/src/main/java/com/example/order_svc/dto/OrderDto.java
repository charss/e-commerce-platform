package com.example.order_svc.dto;

import com.example.order_svc.entity.Order;
import com.example.shared.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public record OrderDto(
        Long id,
        UUID userId,
        List<OrderItemDto> items,
        Double totalAmount,
        OrderStatus status,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        OffsetDateTime timestamp
) {
        public static OrderDto from (Order order) {
                return new OrderDto(
                        order.getId(),
                        order.getUserId(),
                        order.getItems().stream().map(OrderItemDto::from).toList(),
                        order.getTotalAmount(),
                        order.getStatus(),
                        order.getOrderDate()
                );
        }
}
