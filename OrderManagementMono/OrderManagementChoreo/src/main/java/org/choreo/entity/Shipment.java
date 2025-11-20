package org.choreo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.choreo.enums.ShipmentStatus;

import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Shipment {
    @Id
    private String shipmentId;
    private String orderId;
    private String customerId;
    private List<OrderItem> items;
    private ShipmentStatus status;
    private LocalDateTime createdAt;

    public Shipment() {
    }

    public Shipment(String shipmentId, String orderId, String customerId, List<OrderItem> items, ShipmentStatus status, LocalDateTime createdAt) {
        this.shipmentId = shipmentId;
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = items;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(String shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public void setStatus(ShipmentStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
