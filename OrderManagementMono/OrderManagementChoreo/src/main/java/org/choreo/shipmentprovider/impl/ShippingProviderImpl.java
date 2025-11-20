package org.choreo.shipmentprovider.impl;

import org.choreo.entity.OrderItem;
import org.choreo.entity.Shipment;
import org.choreo.shipmentprovider.ShippingProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ShippingProviderImpl implements ShippingProvider {

    private static final Logger log = LoggerFactory.getLogger(ShippingProviderImpl.class);
    private static final int MAX_TOTAL_QUANTITY = 200;

    @Override
    public boolean createShipment(Shipment shipment) {
        if (shipment == null) {
            log.warn("Shipment creation rejected: shipment payload is null");
            return false;
        }

        if (shipment.getItems() == null || shipment.getItems().isEmpty()) {
            log.warn("Shipment creation rejected for order {}: no line items provided", shipment.getOrderId());
            return false;
        }

        int totalQuantity = shipment.getItems().stream()
                .filter(Objects::nonNull)
                .mapToInt(OrderItem::getQuantity)
                .sum();

        if (totalQuantity <= 0) {
            log.warn("Shipment creation rejected for order {}: total quantity must be positive", shipment.getOrderId());
            return false;
        }

        if (totalQuantity > MAX_TOTAL_QUANTITY) {
            log.warn("Shipment creation rejected for order {}: total quantity {} exceeds limit {}", shipment.getOrderId(), totalQuantity, MAX_TOTAL_QUANTITY);
            return false;
        }

        log.info("Shipment provider accepted shipment {} for order {} ({} total units)",
                shipment.getShipmentId(), shipment.getOrderId(), totalQuantity);
        return true;
    }

    @Override
    public boolean cancelShipment(String shipmentId) {
        if (shipmentId == null || shipmentId.isBlank()) {
            log.warn("Shipment cancellation rejected: shipment id is blank");
            return false;
        }

        log.info("Shipment provider acknowledged cancellation for shipment {}", shipmentId);
        return true;
    }
}
