package com.example.order_svc.service;

import com.example.order_svc.dto.OrderStatusDto;
import com.example.order_svc.dto.OrderTimelineDto;
import com.example.order_svc.entity.Order;
import com.example.order_svc.entity.OrderHistory;
import com.example.order_svc.exception.OrderNotFoundException;
import com.example.order_svc.repository.OrderHistoryRepository;
import com.example.order_svc.repository.OrderRepository;
import com.example.shared.enums.OrderStatus;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderHistoryService {
    @Autowired
    OrderHistoryRepository orderHistoryRepo;
    @Autowired
    OrderRepository orderRepo;

    public OrderTimelineDto getOrderTimeline(Long orderId) {
        Order order = orderRepo.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("Order #" + orderId + " not found."));

        List<OrderHistory> orderHistoryList = orderHistoryRepo.findByOrderOrderByChangedAtDesc(order);

        return new OrderTimelineDto(
                orderId,
                orderHistoryList.stream()
                        .map(history -> new OrderStatusDto(history.getStatus(), history.getChangedAt(), history.getRemarks()))
                        .toList()
        );
    }

    public void createOrderHistory(Order order) {
        createOrderHistory(order, "admin", null);
    }

    public void createOrderHistory(Order order, String remarks) {
        createOrderHistory(order, "admin", remarks);
    }

    @Transactional
    public void createOrderHistory(Order order, String changedBy, String remarks) {
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setOrder(order);
        orderHistory.setStatus(order.getStatus());

        if (order.getStatus() == OrderStatus.PENDING) {
            orderHistory.setChangedAt(order.getOrderDate());
        }

        orderHistory.setChangedBy(changedBy);
        orderHistory.setRemarks(remarks);
        orderHistoryRepo.save(orderHistory);
    }
}
