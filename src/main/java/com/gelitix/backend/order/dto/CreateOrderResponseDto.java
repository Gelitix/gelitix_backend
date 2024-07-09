package com.gelitix.backend.order.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateOrderResponseDto {
    private Long orderId;
    private String userName;
    private String eventName;
    private String promo;
    private String ticketType;
    private Integer ticketQuantity;
    private BigDecimal discountPercentage;
    private BigDecimal discountPrice;
    private BigDecimal finalPrice;
}
