package org.choreo.dtos;

import org.choreo.entity.OrderItem;

public class OrderItemDTO {

    private String productId;
    private int quantity;


    public OrderItemDTO() {}

    public OrderItemDTO(String productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public OrderItem toOrderItem() {
        return new OrderItem(productId, quantity);
    }
}
