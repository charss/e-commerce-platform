package com.example.order_svc.controller;

import com.example.order_svc.dto.CreateOrderDto;
import com.example.order_svc.dto.OrderDto;
import com.example.order_svc.dto.OrderTimelineDto;
import com.example.order_svc.dto.UpdateOrderStatusDto;
import com.example.order_svc.service.OrderHistoryService;
import com.example.order_svc.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    OrderHistoryService orderHistoryService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable(value = "id") Long id) {
        OrderDto order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{id}/timeline")
    public ResponseEntity<OrderTimelineDto> getOrderTimelineById(@PathVariable(value = "id") Long id) {
        OrderTimelineDto timeline = orderHistoryService.getOrderTimeline(id);
        return ResponseEntity.ok(timeline);
    }

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@Valid @RequestBody CreateOrderDto orderDto) {
        OrderDto order = orderService.createOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PatchMapping("/status/paid")
    public ResponseEntity<OrderDto> updateToPaidOrder(@Valid @RequestBody UpdateOrderStatusDto updateDto) {
        OrderDto order = orderService.updateToPaid(updateDto);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/status/confirm")
    public ResponseEntity<OrderDto> updateToConfirmedOrder(@Valid @RequestBody UpdateOrderStatusDto updateDto) {
        OrderDto order = orderService.updateToConfirm(updateDto);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/status/cancel")
    public ResponseEntity<OrderDto> updateToCancelOrder(@Valid @RequestBody UpdateOrderStatusDto updateDto) {
        OrderDto order = orderService.updateToCancelled(updateDto);
        return ResponseEntity.ok(order);
    }
}
