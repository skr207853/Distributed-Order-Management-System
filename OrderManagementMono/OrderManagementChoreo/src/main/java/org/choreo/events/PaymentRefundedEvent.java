package org.choreo.events;

public class PaymentRefundedEvent extends DomainEvent{
    private String orderId;
    public PaymentRefundedEvent(String orderId) {
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
