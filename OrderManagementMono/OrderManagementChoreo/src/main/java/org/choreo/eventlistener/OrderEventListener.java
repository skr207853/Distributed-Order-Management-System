package org.choreo.eventlistener;

import org.choreo.events.InventoryReservationFailedEvent;
import org.choreo.events.PaymentFailedEvent;
import org.choreo.events.ShippingCompletedEvent;
import org.choreo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


@Component
public class OrderEventListener {

    @Autowired
    private OrderService orderService;

    @KafkaListener(topics = "payment-events", groupId = "order-service")
    public void handlePaymentFailed(PaymentFailedEvent event) {
        orderService.cancelOrder(event.getOrderId(),
                "Payment failed: " + event.getReason());
    }

    @KafkaListener(topics = "inventory-events", groupId = "order-service")
    public void handleInventoryReservationFailed(InventoryReservationFailedEvent event) {
        orderService.cancelOrder(event.getOrderId(),
                "Inventory reservation failed: " + event.getReason());
    }

    @KafkaListener(topics = "shipping-events", groupId = "order-service")
    public void handleShippingCompleted(ShippingCompletedEvent event) {
        orderService.completeOrder(event.getOrderId());
    }
}
