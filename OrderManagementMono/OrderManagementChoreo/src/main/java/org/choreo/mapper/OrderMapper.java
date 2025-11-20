package org.choreo.mapper;

import org.choreo.dtos.OrderDTO;
import org.choreo.dtos.OrderItemDTO;
import org.choreo.entity.Order;
import org.choreo.entity.OrderItem;
import org.choreo.entity.OrderRequest;
import org.choreo.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    @Autowired
    private OrderItemMapper orderItemMapper;

    public OrderDTO toOrderDTO(Order order) {
        List<OrderItemDTO> itemDTOs = order.getItems().stream()
                .map(orderItemMapper::toOrderItemDTO)
                .collect(Collectors.toList());

        return new OrderDTO(
                order.getOrderId(),
                order.getCustomerId(),
                order.getAmount(),
                order.getStatus(),
                order.getCreatedAt(),
                itemDTOs
        );
    }

    public Order toOrder(OrderDTO orderDTO) {
        List<OrderItem> items = orderDTO.getItemDTOs().stream()
                .map(orderItemMapper::toOrderItem)
                .collect(Collectors.toList());

        return new Order(
                orderDTO.getOrderId(),
                orderDTO.getCustomerId(),
                orderDTO.getAmount(),
                orderDTO.getStatus(),
                items,
                orderDTO.getCreatedAt(),
                orderDTO.getCancellationReason()
        );
    }

}

