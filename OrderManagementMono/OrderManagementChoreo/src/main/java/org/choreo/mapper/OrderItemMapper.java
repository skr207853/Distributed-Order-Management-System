package org.choreo.mapper;

import org.choreo.dtos.OrderItemDTO;
import org.choreo.entity.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {
    public OrderItem toOrderItem(OrderItemDTO orderItemDTO) {
        return new OrderItem(orderItemDTO.getProductId(), orderItemDTO.getQuantity());
    }
    public OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
        return new OrderItemDTO(orderItem.getProductId(), orderItem.getQuantity());
    }

}
