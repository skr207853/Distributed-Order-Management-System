package org.choreo.events;

public class InventoryReservedEvent extends DomainEvent {
    private String orderId;
    private String reservationId;

    public InventoryReservedEvent(String orderId, String reservationId) {
        super(orderId);
        this.orderId = orderId;
        this.reservationId = reservationId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }
}
