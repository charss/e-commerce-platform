package com.example.order_svc.repository;

import com.example.order_svc.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> { }

