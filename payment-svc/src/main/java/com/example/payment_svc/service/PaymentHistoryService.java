package com.example.payment_svc.service;

import com.example.payment_svc.dto.FailedPaymentDto;
import com.example.payment_svc.dto.PaymentStatusDto;
import com.example.payment_svc.dto.PaymentTimelineDto;
import com.example.payment_svc.entity.Payment;
import com.example.payment_svc.entity.PaymentHistory;
import com.example.payment_svc.exception.PaymentNotFoundException;
import com.example.payment_svc.repository.PaymentHistoryRepository;
import com.example.payment_svc.repository.PaymentRepository;
import com.stripe.model.PaymentIntent;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class PaymentHistoryService {
    private final PaymentHistoryRepository paymentHistoryRepo;
    private final PaymentRepository paymentRepo;

    public PaymentHistoryService(PaymentHistoryRepository paymentHistoryRepo, PaymentRepository paymentRepo) {
        this.paymentHistoryRepo = paymentHistoryRepo;
        this.paymentRepo = paymentRepo;
    }

    public PaymentTimelineDto getPaymentTimeline(Long id) {
        Payment payment = this.paymentRepo.findById(id).orElseThrow(
                () -> new PaymentNotFoundException("Payment #" + id + " not found"));

        List<PaymentHistory> paymentHistoryList = this.paymentHistoryRepo.findByPaymentOrderByChangedAtDesc(payment);

        return new PaymentTimelineDto(
                id,
                payment.getPaymentIntentId(),
                paymentHistoryList.stream()
                        .map(history -> new PaymentStatusDto(history.getStatus(), history.getChangedAt(), history.getRemarks()))
                        .toList()
        );
    }

    @Transactional
    public void createPaymentHistory(Payment payment) {
        PaymentHistory history = new PaymentHistory();
        history.setPayment(payment);
        history.setStatus(payment.getStatus());
        history.setChangedAt(payment.getCreatedAt());
        history.setPaymentMethodId(payment.getPaymentMethodId());
        this.paymentHistoryRepo.save(history);
    }

    @Transactional
    public void createPaymentHistory(Payment payment, PaymentIntent intent, OffsetDateTime confirmationTime) {
        PaymentHistory history = new PaymentHistory();
        history.setPayment(payment);
        history.setStatus(intent.getStatus());
        history.setChangedAt(confirmationTime);
        history.setPaymentMethodId(payment.getPaymentMethodId());
        this.paymentHistoryRepo.save(history);
    }

    @Transactional
    public void createPaymentHistory(Payment payment, FailedPaymentDto failedInfo) {
        PaymentHistory history = new PaymentHistory();
        history.setPayment(payment);
        history.setStatus(failedInfo.status());
        history.setChangedAt(Instant.ofEpochSecond(failedInfo.chargedAt()).atOffset(ZoneOffset.UTC));
        history.setPaymentMethodId(payment.getPaymentMethodId());
        history.setRemarks(failedInfo.declineCode());
        this.paymentHistoryRepo.save(history);
    }

    @Transactional
    public void createCancelledPaymentHistory(Payment payment, PaymentIntent intent) {
        PaymentHistory history = new PaymentHistory();
        history.setPayment(payment);
        history.setStatus(intent.getStatus());
        history.setChangedAt(Instant.ofEpochSecond(intent.getCanceledAt()).atOffset(ZoneOffset.UTC));
        history.setPaymentMethodId(payment.getPaymentMethodId());
        history.setRemarks(intent.getCancellationReason());
        this.paymentHistoryRepo.save(history);
    }
}
