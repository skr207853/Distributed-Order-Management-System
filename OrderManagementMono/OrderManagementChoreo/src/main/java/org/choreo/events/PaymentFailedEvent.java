package org.choreo.events;

public class PaymentFailedEvent extends DomainEvent {
    private String orderId;
    private String reason;

    public PaymentFailedEvent(String orderId, String reason) {
        super(orderId);
        this.orderId = orderId;
        this.reason = reason;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
