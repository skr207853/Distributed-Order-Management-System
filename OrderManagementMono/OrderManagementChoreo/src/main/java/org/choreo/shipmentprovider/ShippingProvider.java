package org.choreo.shipmentprovider;

import org.choreo.entity.Shipment;

public interface ShippingProvider {

    boolean createShipment(Shipment shipment);

    boolean cancelShipment(String shipmentId);
}
