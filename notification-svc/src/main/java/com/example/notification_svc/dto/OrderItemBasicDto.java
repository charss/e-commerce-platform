package com.example.notification_svc.dto;

public record OrderItemBasicDto(
        String sku,
        Integer quantity,
        Double pricePerUnit
) { }
