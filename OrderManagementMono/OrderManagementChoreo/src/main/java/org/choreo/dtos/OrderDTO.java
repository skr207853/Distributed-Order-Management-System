package org.choreo.dtos;

import org.choreo.entity.OrderItem;
import org.choreo.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDTO {

    private String orderId;
    private String customerId;
    private BigDecimal amount;
    private OrderStatus status;
    private LocalDateTime createdAt;
    private String cancellationReason;

    private List<OrderItemDTO> itemDTOs;


    public OrderDTO() {}


    public OrderDTO(String orderId, String customerId, BigDecimal amount,
                    OrderStatus status, LocalDateTime createdAt, List<OrderItemDTO> itemDTOs) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.amount = amount;
        this.status = status;
        this.createdAt = createdAt;
        this.itemDTOs = itemDTOs;
    }


    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setItemDTOs(List<OrderItemDTO> itemDTOs) {
        this.itemDTOs = itemDTOs;
    }

    public List<OrderItemDTO> getItemDTOs() {
        return itemDTOs;
    }

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

}