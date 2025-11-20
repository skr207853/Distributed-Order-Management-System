package org.choreo.events;

public class ShippingCancelledEvent extends DomainEvent {
    private String orderId;
    private String shipmentId;

    public ShippingCancelledEvent(String orderId, String shipmentId) {
        super(orderId);
        this.orderId = orderId;
        this.shipmentId = shipmentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getShipmentId() {
        return shipmentId;
    }
}
