package org.choreo.service;

import org.choreo.entity.Payment;
import org.choreo.enums.PaymentStatus;
import org.choreo.events.DomainEvent;
import org.choreo.events.PaymentFailedEvent;
import org.choreo.events.PaymentProcessedEvent;
import org.choreo.events.PaymentRefundedEvent;
import org.choreo.exceptionhandling.exceptions.PaymentNotFoundException;
import org.choreo.gateway.PaymentGateway;
import org.choreo.repositories.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

//Payment Service
@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentGateway paymentGateway;

    @Autowired
    private KafkaTemplate<String, DomainEvent> kafkaTemplate;

    private static final String PAYMENT_EVENTS_TOPIC = "payment-events";

    @Transactional
    public void processPayment(String orderId, String customerId, BigDecimal amount) {
        try {
            Payment payment = new Payment();
            payment.setPaymentId(UUID.randomUUID().toString());
            payment.setOrderId(orderId);
            payment.setCustomerId(customerId);
            payment.setAmount(amount);
            payment.setStatus(PaymentStatus.PENDING);

            boolean paymentSuccess = paymentGateway.charge(customerId, amount);

            if (paymentSuccess) {
                payment.setStatus(PaymentStatus.COMPLETED);
                paymentRepository.save(payment);

                PaymentProcessedEvent event = new PaymentProcessedEvent(
                        orderId, payment.getPaymentId()
                );
                kafkaTemplate.send(PAYMENT_EVENTS_TOPIC, orderId, event);
            } else {
                payment.setStatus(PaymentStatus.FAILED);
                paymentRepository.save(payment);

                PaymentFailedEvent event = new PaymentFailedEvent(
                        orderId, "Payment gateway declined"
                );
                kafkaTemplate.send(PAYMENT_EVENTS_TOPIC, orderId, event);
            }
        } catch (Exception e) {
            PaymentFailedEvent event = new PaymentFailedEvent(
                    orderId, "Payment processing error: " + e.getMessage()
            );
            kafkaTemplate.send(PAYMENT_EVENTS_TOPIC, orderId, event);
        }
    }

    @Transactional
    public void refundPayment(String orderId) {
        // Compensating transaction
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(() -> new PaymentNotFoundException(orderId));

        if (payment.getStatus() == PaymentStatus.COMPLETED) {
            paymentGateway.refund(payment.getPaymentId());
            payment.setStatus(PaymentStatus.REFUNDED);
            paymentRepository.save(payment);

            PaymentRefundedEvent event = new PaymentRefundedEvent(orderId);
            kafkaTemplate.send(PAYMENT_EVENTS_TOPIC, orderId, event);
        }
    }
}