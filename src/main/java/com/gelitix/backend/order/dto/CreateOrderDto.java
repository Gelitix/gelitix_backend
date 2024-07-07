package com.gelitix.backend.order.dto;

import com.gelitix.backend.event.entity.Event;
import com.gelitix.backend.promoDetail.entity.PromoDetail;
import com.gelitix.backend.users.entity.Users;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateOrderDto {
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
}
