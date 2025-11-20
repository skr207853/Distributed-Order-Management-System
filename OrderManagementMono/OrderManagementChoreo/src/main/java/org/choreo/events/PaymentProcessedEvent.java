package org.choreo.events;

public class PaymentProcessedEvent extends DomainEvent {
    private String orderId;
    private String paymentId;

    public PaymentProcessedEvent(String orderId, String paymentId) {
        super(orderId);
        this.orderId = orderId;
        this.paymentId = paymentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
}
