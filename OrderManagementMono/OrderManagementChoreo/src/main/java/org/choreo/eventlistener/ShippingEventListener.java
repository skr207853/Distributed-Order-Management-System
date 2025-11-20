package org.choreo.eventlistener;

import org.choreo.clients.OrderClient;
import org.choreo.dtos.OrderDTO;
import org.choreo.events.InventoryReservedEvent;
import org.choreo.events.OrderCancelledEvent;
import org.choreo.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ShippingEventListener {

    @Autowired
    private ShippingService shippingService;

    @Autowired
    private OrderClient orderClient;

    @KafkaListener(topics = "inventory-events", groupId = "shipping-service")
    public void handleInventoryReserved(InventoryReservedEvent event) {
        OrderDTO order = orderClient.getOrder(event.getOrderId());
        shippingService.createShipment(
                event.getOrderId(),
                order.getCustomerId(),
                order.getItems()
        );
    }

    @KafkaListener(topics = "order-events", groupId = "shipping-service")
    public void handleOrderCancelled(OrderCancelledEvent event) {
        shippingService.cancelShipment(event.getOrderId());
    }
}


