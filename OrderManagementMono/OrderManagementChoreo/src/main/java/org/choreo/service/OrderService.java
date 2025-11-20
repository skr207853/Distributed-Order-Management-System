package org.choreo.service;

import jakarta.transaction.Transactional;
import org.choreo.entity.OrderItem;
import org.choreo.enums.OrderStatus;
import org.choreo.entity.Order;
import org.choreo.events.DomainEvent;
import org.choreo.events.OrderCancelledEvent;
import org.choreo.events.OrderCompletedEvent;
import org.choreo.events.OrderCreatedEvent;
import org.choreo.exceptionhandling.exceptions.OrderNotFoundException;
import org.choreo.mapper.OrderItemMapper;
import org.choreo.mapper.OrderMapper;
import org.choreo.repositories.OrderRepository;
import org.choreo.entity.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Autowired
    private KafkaTemplate<String, DomainEvent> kafkaTemplate;

    private static final String ORDER_EVENTS_TOPIC = "order-events";

    @Transactional
    public Order createOrder(OrderRequest request) {

        Order order = new Order();
        order.setOrderId(UUID.randomUUID().toString());
        order.setCustomerId(request.getCustomerId());
        order.setAmount(request.getAmount());
        order.setItems(request.getItemsDTOs().stream().map(orderItemMapper::toOrderItem).toList());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        orderRepository.save(order);

        OrderCreatedEvent event = new OrderCreatedEvent(
                order.getOrderId(),
                order.getCustomerId(),
                order.getAmount(),
                order.getItems()
        );

        kafkaTemplate.send(ORDER_EVENTS_TOPIC, order.getOrderId(), event);

        return order;
    }

    @Transactional
    public void completeOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.setStatus(OrderStatus.COMPLETED);
        orderRepository.save(order);

        OrderCompletedEvent event = new OrderCompletedEvent(orderId);
        kafkaTemplate.send(ORDER_EVENTS_TOPIC, orderId, event);
    }

    @Transactional
    public void cancelOrder(String orderId, String reason) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        order.setStatus(OrderStatus.CANCELLED);
        order.setCancellationReason(reason);
        orderRepository.save(order);

        OrderCancelledEvent event = new OrderCancelledEvent(orderId, reason);
        kafkaTemplate.send(ORDER_EVENTS_TOPIC, orderId, event);
    }
}