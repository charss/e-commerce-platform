package com.example.payment_svc.controller;

import com.example.payment_svc.dto.PaymentIntentDto;
import com.example.payment_svc.dto.PaymentTimelineDto;
import com.example.payment_svc.exception.CardDeclinedException;
import com.example.payment_svc.service.PaymentHistoryService;
import com.example.payment_svc.service.PaymentService;
import com.example.shared.dto.PaymentConfirmDto;
import com.example.shared.dto.PaymentRequestDto;
import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {
    private final PaymentService paymentService;
    private final PaymentHistoryService paymentHistoryService;

    public PaymentController(PaymentService paymentService, PaymentHistoryService paymentHistoryService) {
        this.paymentService = paymentService;
        this.paymentHistoryService = paymentHistoryService;
    }

    @GetMapping("/{id}/timeline")
    public ResponseEntity<PaymentTimelineDto> getPaymentTimelineById(@PathVariable(value = "id") Long id) {
        PaymentTimelineDto timeline = paymentHistoryService.getPaymentTimeline(id);
        return ResponseEntity.ok(timeline);
    }

    @PostMapping("/create-payment-intent")
    public ResponseEntity<PaymentIntentDto> createPaymentIntent(@Valid @RequestBody PaymentRequestDto dto) throws StripeException {
        PaymentIntentDto response = this.paymentService.createPaymentIntent(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/confirm")
    public ResponseEntity<PaymentIntentDto> confirmPayment(@Valid @RequestBody PaymentConfirmDto dto) throws CardDeclinedException {
        PaymentIntentDto response = this.paymentService.confirmPaymentIntent(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<PaymentIntentDto> cancelPaymentIntent(@PathVariable(value = "id") String paymentIntentId) {
        PaymentIntentDto response = this.paymentService.cancelPaymentIntent(paymentIntentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
