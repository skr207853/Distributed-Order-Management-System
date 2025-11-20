package org.choreo.eventlistener;

import org.choreo.clients.OrderClient;
import org.choreo.dtos.OrderDTO;
import org.choreo.events.OrderCancelledEvent;
import org.choreo.events.PaymentProcessedEvent;
import org.choreo.events.ShippingCompletedEvent;
import org.choreo.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class InventoryEventListener {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private OrderClient orderClient;

    @KafkaListener(topics = "payment-events", groupId = "inventory-service")
    public void handlePaymentProcessed(PaymentProcessedEvent event) {
        OrderDTO order = orderClient.getOrder(event.getOrderId());
        inventoryService.reserveInventory(event.getOrderId(), order.getItems());
    }

    @KafkaListener(topics = "order-events", groupId = "inventory-service")
    public void handleOrderCancelled(OrderCancelledEvent event) {
        inventoryService.releaseReservation(event.getOrderId());
    }

    @KafkaListener(topics = "shipping-events", groupId = "inventory-service")
    public void handleShippingCompleted(ShippingCompletedEvent event) {
        inventoryService.confirmReservation(event.getOrderId());
    }
}

