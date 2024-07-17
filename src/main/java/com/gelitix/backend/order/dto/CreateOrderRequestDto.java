package com.gelitix.backend.order.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    private BigDecimal pointUsed;

    @NotBlank
    private String fullName;

    @NotBlank
    @Size(max = 20)
    private String phoneNumber;

    @NotBlank
    @Size(max = 50)
    private String email;

    @NotBlank
    private String identityCard;
}
