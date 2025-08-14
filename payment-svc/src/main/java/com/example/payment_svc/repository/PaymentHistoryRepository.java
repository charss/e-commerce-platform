package com.example.payment_svc.repository;

import com.example.payment_svc.entity.Payment;
import com.example.payment_svc.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {
    List<PaymentHistory> findByPaymentOrderByChangedAtDesc(Payment payment);
}
