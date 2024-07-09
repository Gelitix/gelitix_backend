package com.gelitix.backend.dashboard.dto;

import com.gelitix.backend.order.dao.EventAttendeeCountDao;
import com.gelitix.backend.order.dao.PeriodicalRevenueDao;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class EventStatisticsDTO {
    private BigDecimal totalRevenue;
    private Double totalOrders;
    private Double totalCustomers;
    private List<PeriodicalRevenueDao> yearlyRevenue;
    private List<PeriodicalRevenueDao> monthlyRevenue;
    private List<PeriodicalRevenueDao> dailyRevenue;


}