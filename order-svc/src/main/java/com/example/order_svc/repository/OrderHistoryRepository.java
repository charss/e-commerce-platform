package com.example.order_svc.repository;

import com.example.order_svc.entity.Order;
import com.example.order_svc.entity.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
    List<OrderHistory> findByOrderOrderByChangedAtDesc(Order order);
}
