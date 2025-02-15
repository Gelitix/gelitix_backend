package com.gelitix.backend.promoDetail.dto;

import com.gelitix.backend.event.entity.Event;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class CreatePromoDto {
    @Nullable
    private Boolean isReferral;

    @NotBlank
    private Integer quantity;

    @Size(max = 50)
    @NotBlank
    private String name;

    @NotBlank
    private Long eventId;

    @NotBlank
    private BigDecimal discount;

    @NotBlank
    private Instant startValid;

    @NotBlank
    private Instant endValid;



}
