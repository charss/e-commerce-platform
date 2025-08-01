package com.example.order_svc.dto;

import com.example.shared.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.OffsetDateTime;

public record OrderStatusDto(
        OrderStatus status,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
        OffsetDateTime timestamp,
        String remarks
) { }
