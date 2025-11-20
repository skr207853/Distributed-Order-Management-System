package org.choreo.entity;

import org.choreo.enums.ReservationStatus;

public class Reservation {
    private String reservationId;
    private String orderId;
    private String productId;
    private int reservationQuantity;
    private ReservationStatus status;

    public Reservation() {
    }

    public Reservation(String reservationId, String orderId, String productId, int reservationQuantity, ReservationStatus status) {
        this.reservationId = reservationId;
        this.orderId = orderId;
        this.productId = productId;
        this.reservationQuantity = reservationQuantity;
        this.status = status;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getReservationQuantity() {
        return reservationQuantity;
    }

    public void setQuantity(int quantity) {
        this.reservationQuantity = quantity;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}
