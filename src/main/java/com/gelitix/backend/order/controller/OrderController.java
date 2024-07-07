package com.gelitix.backend.order.controller;

import com.gelitix.backend.auth.helpers.Claims;
import com.gelitix.backend.order.dto.CreateOrderDto;
import com.gelitix.backend.order.service.OrderService;
import com.gelitix.backend.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/order")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("")
    public ResponseEntity<?> createOrder(@RequestBody CreateOrderDto createOrderDto ) {
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        var createdOrder = orderService.createOrder(createOrderDto, email);
        return Response.success(200, "Order created", createdOrder);
    }
}
