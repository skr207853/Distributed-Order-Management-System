package org.choreo.events;

public class ShippingFailedEvent extends DomainEvent {
    private String orderId;
    private String message;
    public ShippingFailedEvent(String orderId, String message) {
        super(orderId);
        this.orderId = orderId;
        this.message = message;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
