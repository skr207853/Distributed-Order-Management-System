package org.choreo.service;

import org.choreo.entity.OrderItem;
import org.choreo.entity.Shipment;
import org.choreo.enums.ShipmentStatus;
import org.choreo.events.DomainEvent;
import org.choreo.events.ShippingCancelledEvent;
import org.choreo.events.ShippingCompletedEvent;
import org.choreo.events.ShippingFailedEvent;
import org.choreo.repositories.ShipmentRepository;
import org.choreo.shipmentprovider.ShippingProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
public class ShippingService {

    @Autowired
    private ShipmentRepository shipmentRepository;

    @Autowired
    private ShippingProvider shippingProvider;

    @Autowired
    private KafkaTemplate<String, DomainEvent> kafkaTemplate;

    private static final String SHIPPING_EVENTS_TOPIC = "shipping-events";

    @Transactional
    public void createShipment(String orderId, String customerId, List<OrderItem> items) {
        try {
            Shipment shipment = new Shipment();
            shipment.setShipmentId(UUID.randomUUID().toString());
            shipment.setOrderId(orderId);
            shipment.setCustomerId(customerId);
            shipment.setItems(items);
            shipment.setStatus(ShipmentStatus.CREATED);
            shipment.setCreatedAt(LocalDateTime.now());

            shipmentRepository.save(shipment);

            // Simulate shipment creation
            boolean shipmentCreated = shippingProvider.createShipment(shipment);

            if (shipmentCreated) {
                shipment.setStatus(ShipmentStatus.SHIPPED);
                shipmentRepository.save(shipment);

                ShippingCompletedEvent event = new ShippingCompletedEvent(
                        orderId, shipment.getShipmentId()
                );
                kafkaTemplate.send(SHIPPING_EVENTS_TOPIC, orderId, event);
            } else {
                ShippingFailedEvent event = new ShippingFailedEvent(
                        orderId, "Shipping provider error"
                );
                kafkaTemplate.send(SHIPPING_EVENTS_TOPIC, orderId, event);
            }
        } catch (Exception e) {
            ShippingFailedEvent event = new ShippingFailedEvent(
                    orderId, "Shipment creation error: " + e.getMessage()
            );
            kafkaTemplate.send(SHIPPING_EVENTS_TOPIC, orderId, event);
        }
    }

    @Transactional
    public void cancelShipment(String orderId) {
        // Compensating transaction
        Shipment shipment = shipmentRepository.findByOrderId(orderId)
                .orElse(null);

        if (shipment != null && shipment.getStatus() == ShipmentStatus.SHIPPED) {
            shippingProvider.cancelShipment(shipment.getShipmentId());
            shipment.setStatus(ShipmentStatus.CANCELLED);
            shipmentRepository.save(shipment);

            ShippingCancelledEvent event = new ShippingCancelledEvent(shipment.getOrderId(), shipment.getShipmentId());
            kafkaTemplate.send(SHIPPING_EVENTS_TOPIC, orderId, event);
        }
    }
}

