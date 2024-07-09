package com.gelitix.backend.order.dao;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Date;

public interface PeriodicalRevenueDao {
    Date getCreatedDate();
    BigDecimal getFinalPrice();
}
