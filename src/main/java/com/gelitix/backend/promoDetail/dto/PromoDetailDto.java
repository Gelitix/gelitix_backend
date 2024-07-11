package com.gelitix.backend.promoDetail.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class PromoDetailDto {
    private Long id;
    private String name;
    private BigDecimal discount;
    private Instant startValid;
    private Instant endValid;
    private Integer quantity;
    private Boolean isReferral;
    private Long eventId;
}
