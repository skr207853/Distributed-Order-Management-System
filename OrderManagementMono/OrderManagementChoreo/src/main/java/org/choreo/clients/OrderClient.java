package org.choreo.clients;

import org.choreo.dtos.OrderDTO;
import org.choreo.entity.OrderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(
        name = "order-service",
        url = "${services.order.base-url}",
        contextId = "orderClient"
)
public interface OrderClient {

    @GetMapping("/api/orders/{orderId}")
    OrderDTO getOrder(@PathVariable String orderId);

    @PostMapping("/api/orders")
    OrderDTO createOrder(@RequestBody OrderRequest request);
}