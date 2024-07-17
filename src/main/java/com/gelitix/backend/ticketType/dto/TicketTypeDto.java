package com.gelitix.backend.ticketType.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TicketTypeDto {
    private String name;
    private BigDecimal price;
    private Integer quantity;
}