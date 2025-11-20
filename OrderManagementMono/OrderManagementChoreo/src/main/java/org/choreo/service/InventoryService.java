package org.choreo.service;

import org.choreo.events.InventoryReleasedEvent;
import org.choreo.events.InventoryReservationFailedEvent;
import org.choreo.events.InventoryReservedEvent;
import org.choreo.exceptionhandling.exceptions.ProductNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.choreo.entity.Inventory;
import org.choreo.entity.OrderItem;
import org.choreo.entity.Reservation;
import org.choreo.enums.ReservationStatus;
import org.choreo.events.DomainEvent;
import org.choreo.repositories.InventoryRepository;
import org.choreo.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private KafkaTemplate<String, DomainEvent> kafkaTemplate;

    private static final String INVENTORY_EVENTS_TOPIC = "inventory-events";

    @Transactional
    public void reserveInventory(String orderId, List<OrderItem> items) {
        try {
            String reservationId = UUID.randomUUID().toString();

            // Check and reserve inventory for all items
            for (OrderItem item : items) {
                Inventory inventory = inventoryRepository
                        .findByProductId(item.getProductId())
                        .orElseThrow(() -> new ProductNotFoundException(item.getProductId()));

                if (inventory.getAvailableQuantity() < item.getQuantity()) {
                    // Insufficient inventory
                    InventoryReservationFailedEvent event =
                            new InventoryReservationFailedEvent(
                                    orderId,
                                    "Insufficient inventory for product: " + item.getProductId()
                            );
                    kafkaTemplate.send(INVENTORY_EVENTS_TOPIC, orderId, event);
                    return;
                }

                // Reserve inventory
                inventory.setAvailableQuantity(
                        inventory.getAvailableQuantity() - item.getQuantity()
                );
                inventory.setReservedQuantity(
                        inventory.getReservedQuantity() + item.getQuantity()
                );
                inventoryRepository.save(inventory);

                // Create reservation record
                Reservation reservation = new Reservation();
                reservation.setReservationId(reservationId);
                reservation.setOrderId(orderId);
                reservation.setProductId(item.getProductId());
                reservation.setQuantity(item.getQuantity());
                reservation.setStatus(ReservationStatus.RESERVED);
                reservationRepository.save(reservation);
            }

            // Publish success event
            InventoryReservedEvent event = new InventoryReservedEvent(
                    orderId, reservationId
            );
            kafkaTemplate.send(INVENTORY_EVENTS_TOPIC, orderId, event);

        } catch (Exception e) {
            InventoryReservationFailedEvent event =
                    new InventoryReservationFailedEvent(
                            orderId,
                            "Inventory reservation error: " + e.getMessage()
                    );
            kafkaTemplate.send(INVENTORY_EVENTS_TOPIC, orderId, event);
        }
    }

    @Transactional
    public void releaseReservation(String orderId) {
        // Compensating transaction
        List<Reservation> reservations = reservationRepository.findByOrderId(orderId);

        for (Reservation reservation : reservations) {
            if (reservation.getStatus() == ReservationStatus.RESERVED) {
                Inventory inventory = inventoryRepository
                        .findByProductId(reservation.getProductId())
                        .orElseThrow();

                // Release reserved inventory
                inventory.setAvailableQuantity(
                        inventory.getAvailableQuantity() + reservation.getReservationQuantity()
                );
                inventory.setReservedQuantity(
                        inventory.getReservedQuantity() - reservation.getReservationQuantity()
                );
                inventoryRepository.save(inventory);

                reservation.setStatus(ReservationStatus.RELEASED);
                reservationRepository.save(reservation);
            }
        }

        InventoryReleasedEvent event = new InventoryReleasedEvent(orderId);
        kafkaTemplate.send(INVENTORY_EVENTS_TOPIC, orderId, event);
    }

    @Transactional
    public void confirmReservation(String orderId) {
        List<Reservation> reservations = reservationRepository.findByOrderId(orderId);

        for (Reservation reservation : reservations) {
            Inventory inventory = inventoryRepository
                    .findByProductId(reservation.getProductId())
                    .orElseThrow();

            // Deduct from reserved quantity
            inventory.setReservedQuantity(
                    inventory.getReservedQuantity() - reservation.getReservationQuantity()
            );
            inventoryRepository.save(inventory);

            reservation.setStatus(ReservationStatus.CONFIRMED);
            reservationRepository.save(reservation);
        }
    }
}
