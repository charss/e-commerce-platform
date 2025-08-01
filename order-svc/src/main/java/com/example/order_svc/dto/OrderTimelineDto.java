package com.example.order_svc.dto;

import java.util.List;

public record OrderTimelineDto(
        Long orderId,
        List<OrderStatusDto> timeline
) { }
