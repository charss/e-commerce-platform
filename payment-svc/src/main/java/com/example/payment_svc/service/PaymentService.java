package com.example.payment_svc.service;

import com.example.payment_svc.dto.FailedPaymentDto;
import com.example.payment_svc.dto.PaymentIntentDto;
import com.example.payment_svc.dto.PaymentWithIntentDto;
import com.example.payment_svc.entity.Payment;
import com.example.payment_svc.exception.CardDeclinedException;
import com.example.payment_svc.exception.PaymentMethodRequiredException;
import com.example.payment_svc.exception.PaymentNotFoundException;
import com.example.payment_svc.repository.PaymentRepository;
import com.example.shared.dto.PaymentConfirmDto;
import com.example.shared.dto.PaymentRequestDto;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentConfirmParams;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
public class PaymentService {
    private final PaymentRepository paymentRepo;
    private final PaymentHistoryService paymentHistoryService;

    public PaymentService(PaymentRepository paymentRepo, PaymentHistoryService paymentHistoryService) {
        this.paymentRepo = paymentRepo;
        this.paymentHistoryService = paymentHistoryService;
    }

    @Transactional
    public PaymentIntentDto createPaymentIntent(PaymentRequestDto dto) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(dto.amount())
                .setCurrency(dto.currency())
                .setPaymentMethod(dto.payment_method())
                .build();

        PaymentIntent pi = PaymentIntent.create(params);
        Payment payment = new Payment(pi);

        // TODO: Add verification for both userId and orderId
        payment.setUserId(dto.userId());
        payment.setOrderId(dto.orderId());
        this.paymentRepo.save(payment);
        this.paymentHistoryService.createPaymentHistory(payment);

        return PaymentIntentDto.from(pi);
    }

    @Transactional
    public PaymentIntentDto confirmPaymentIntent(PaymentConfirmDto dto) throws CardDeclinedException {
        PaymentWithIntentDto result = this.getOrCreatePayment(dto.paymentIntentId());
        Payment payment = result.payment();
        PaymentIntent paymentIntent = result.paymentIntent();

        try {
            PaymentIntentConfirmParams.Builder paramsBuilder =
                    PaymentIntentConfirmParams.builder()
                        .setReturnUrl("https://example.com/payment-return");

            if (paymentIntent.getPaymentMethod() == null) {
                if (dto.paymentMethodId() == null) {
                    throw new PaymentMethodRequiredException("Payment method required for confirmation");
                }

                payment.setPaymentMethodId(dto.paymentMethodId());
                paramsBuilder.setPaymentMethod(dto.paymentMethodId());
            }

            PaymentIntentConfirmParams params = paramsBuilder.build();
            PaymentIntent confirmed = paymentIntent.confirm(params);

            String chargeId = confirmed.getLatestCharge();
            OffsetDateTime confirmationTime = null;
            if (chargeId != null) {
                Charge charge = Charge.retrieve(chargeId);
                confirmationTime = Instant.ofEpochSecond(charge.getCreated()).atOffset(ZoneOffset.UTC);
            }
            payment.setStatus(confirmed.getStatus());
            payment.setConfirmedTime(confirmationTime);
            this.paymentRepo.save(payment);
            this.paymentHistoryService.createPaymentHistory(payment, confirmed, confirmationTime);

            return PaymentIntentDto.from(confirmed);
        } catch (CardException e) {
            // Declined card or authentication failure
            FailedPaymentDto failedInfo = retrieveFailedPaymentIntent(e);
            payment.setStatus(failedInfo.status());
            payment.setRemarks(failedInfo.declineCode());
            this.paymentRepo.save(payment);
            this.paymentHistoryService.createPaymentHistory(payment, failedInfo);

            if ("insufficient_funds".equals(e.getDeclineCode())) {
                throw new CardDeclinedException("Card error: Insufficient Funds");
            }
            throw new RuntimeException("Card error: " + e.getMessage());
        } catch (StripeException e) {
            throw new RuntimeException("Stripe error: " + e.getMessage());
        }
        // TODO: Add other errors
    }

    @Transactional
    public PaymentIntentDto cancelPaymentIntent(String paymentIntentId) {
        PaymentWithIntentDto result = this.getOrCreatePayment(paymentIntentId);
        Payment payment = result.payment();
        PaymentIntent paymentIntent = result.paymentIntent();


        try {
            PaymentIntent canceledIntent = paymentIntent.cancel();
            payment.setStatus(canceledIntent.getStatus());
            payment.setRemarks(canceledIntent.getCancellationReason());
            this.paymentRepo.save(payment);
            this.paymentHistoryService.createCancelledPaymentHistory(payment, canceledIntent);

            return PaymentIntentDto.from(canceledIntent);
        } catch (StripeException e) {
            throw new RuntimeException("Failed to cancel PaymentIntent: " + paymentIntentId, e);
        }
    }

    private FailedPaymentDto retrieveFailedPaymentIntent (String paymentIntentId) {
        try {
            PaymentIntent latestIntent = PaymentIntent.retrieve(paymentIntentId);

            String status = latestIntent.getStatus();
            Long chargedAt = null;
            String declineCode = null;

            if (latestIntent.getLatestCharge() != null) {
                Charge latestCharge = Charge.retrieve(latestIntent.getLatestCharge());
                chargedAt = latestCharge.getCreated();
                declineCode = latestCharge.getOutcome().getReason();
            }

            return new FailedPaymentDto(paymentIntentId, status, declineCode, chargedAt);
        } catch (StripeException ex) {
            throw new RuntimeException("Unable to retrieve failed Payment Intent: " + paymentIntentId, ex);
        }
    }

    private FailedPaymentDto retrieveFailedPaymentIntent(StripeException ex) {
        if (ex.getStripeError() != null && ex.getStripeError().getPaymentIntent() != null) {
            PaymentIntent errorIntent = (PaymentIntent) ex.getStripeError().getPaymentIntent();
            return retrieveFailedPaymentIntent(errorIntent.getId());
        }
        throw new IllegalArgumentException("StripeException does not contain a PaymentIntent");
    }

    private PaymentWithIntentDto getOrCreatePayment(String paymentIntentId) {
        PaymentIntent paymentIntent;
        try {
            paymentIntent = PaymentIntent.retrieve(paymentIntentId);
        } catch (StripeException e) {
            throw new PaymentNotFoundException("Payment Intent not found in Stipe: " + paymentIntentId);
        }

        Payment payment = this.paymentRepo.findByPaymentIntentId(paymentIntentId)
                .orElseGet(() -> paymentRepo.save(new Payment(paymentIntent)));

        return new PaymentWithIntentDto(payment, paymentIntent);
    }
}