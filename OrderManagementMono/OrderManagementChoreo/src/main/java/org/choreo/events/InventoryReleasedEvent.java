package org.choreo.events;

public class InventoryReleasedEvent extends DomainEvent{
    private String orderId;

    public InventoryReleasedEvent(String orderId) {
        super(orderId);
        this.orderId = orderId;
    }
}
