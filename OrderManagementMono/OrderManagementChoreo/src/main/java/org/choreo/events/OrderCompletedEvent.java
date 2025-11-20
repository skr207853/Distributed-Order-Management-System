package org.choreo.events;

public class OrderCompletedEvent extends DomainEvent {
    private String orderId;

    public OrderCompletedEvent(String orderId) {
        super(orderId);
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
