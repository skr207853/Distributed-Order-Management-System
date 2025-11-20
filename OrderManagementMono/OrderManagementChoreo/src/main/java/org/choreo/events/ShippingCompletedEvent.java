package org.choreo.events;

public class ShippingCompletedEvent extends DomainEvent {
    private String orderId;
    private String shipmentId;

    public ShippingCompletedEvent(String orderId, String shipmentId) {
        super(orderId);
        this.orderId=orderId;
        this.shipmentId = shipmentId;
    }

    public String getOrderId() {
        return orderId;
    }
    public String getShipmentId() {
        return shipmentId;
    }
}
