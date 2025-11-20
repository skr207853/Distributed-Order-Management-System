package org.choreo.entity;

import org.choreo.dtos.OrderItemDTO;

import java.math.BigDecimal;
import java.util.List;

public class OrderRequest {
    private String orderId;
    private String customerId;
    private BigDecimal amount;
    private List<OrderItemDTO> itemDTOs;

    public OrderRequest() {}

    public OrderRequest(String orderId, String customerId, BigDecimal amount, List<OrderItemDTO> itemDTOs) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.amount = amount;
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


    public List<OrderItemDTO> getItemsDTOs() {
        return itemDTOs;
    }

    public void setItemsDTOs(List<OrderItemDTO> itemsDTO) {
        this.itemDTOs = itemDTOs;
    }
}
