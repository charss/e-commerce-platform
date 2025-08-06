package com.example.notification_svc.dto.data;

import com.example.notification_svc.dto.OrderItemBasicDto;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record OrderConfirmedData(
        @JsonProperty(required = true) String email,
        @JsonProperty(required = true) String firstName,
        @JsonProperty(required = true) String orderId,
        @JsonProperty(required = true) String orderDate,
        @JsonProperty(required = true) List<OrderItemBasicDto> items,
        @JsonProperty(required = true) String total
) { }
