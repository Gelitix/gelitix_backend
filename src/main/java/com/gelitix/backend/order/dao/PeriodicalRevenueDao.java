package com.gelitix.backend.order.dao;

import java.math.BigDecimal;
import java.time.Instant;

public interface PeriodicalRevenueDao {
    Instant getCreatedDate();
    BigDecimal getFinalPrice();
}
