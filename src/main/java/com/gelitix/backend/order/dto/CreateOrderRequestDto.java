package com.gelitix.backend.order.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateOrderRequestDto {
    @NotBlank
    private Integer ticketQuantity;

    @NotBlank
    private Long eventId;

    @Nullable
    private Long promoId;

    @NotBlank
    private Long ticketTypeId;

    @Nullable
    private Long promoTypeId;

    @Nullable
    private BigDecimal pointUsed;
}
