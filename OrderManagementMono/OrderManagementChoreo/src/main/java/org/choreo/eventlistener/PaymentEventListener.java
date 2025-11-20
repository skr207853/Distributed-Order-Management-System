package org.choreo.eventlistener;

import org.choreo.events.OrderCancelledEvent;
import org.choreo.events.OrderCreatedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.choreo.service.PaymentService;

@Component
public class PaymentEventListener {

    @Autowired
    private PaymentService paymentService;

    @KafkaListener(topics = "order-events", groupId = "payment-service")
    public void handleOrderCreated(OrderCreatedEvent event) {
        // Start payment processing
        paymentService.processPayment(
                event.getOrderId(),
                event.getCustomerId(),
                event.getAmount()
        );
    }

    @KafkaListener(topics = "order-events", groupId = "payment-service")
    public void handleOrderCancelled(OrderCancelledEvent event) {
        paymentService.refundPayment(event.getOrderId());
    }
}