package com.gelitix.backend.dashboard.service.impl;

import com.gelitix.backend.dashboard.dto.EventStatisticsDTO;
import com.gelitix.backend.dashboard.service.DashboardService;
import com.gelitix.backend.event.service.EventService;
import com.gelitix.backend.order.dao.EventAttendeeCountDao;
import com.gelitix.backend.order.dao.PeriodicalRevenueDao;
import com.gelitix.backend.order.service.OrderService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DashboardServiceImpl implements DashboardService {
    @Lazy
    private final OrderService orderService;
    private final EventService eventService;

    public DashboardServiceImpl(OrderService orderService, EventService eventService) {
        this.orderService = orderService;
        this.eventService = eventService;
    }

    @Override
    public EventStatisticsDTO getEventStatistics(Long eventId, String email) {
     String eventName =  eventService.findEventById(eventId).getName();
     EventAttendeeCountDao eventAttendees = orderService.countAttendeesByEventId(eventId);
     BigDecimal totalRevenuePerEvent = BigDecimal.valueOf(orderService.countTotalRevenueByEvent(eventId));
     List<PeriodicalRevenueDao> dailyRevenue = orderService.findDailyRevenueByEventId(eventId);
     List<PeriodicalRevenueDao> monthlyRevenue = orderService.findMonthlyRevenueByEventId(eventId);
     List<PeriodicalRevenueDao> yearlyRevenue = orderService.findYearlyRevenueByEventId(eventId);

     EventStatisticsDTO eventStatisticsDTO = new EventStatisticsDTO();
     eventStatisticsDTO.setEventName(eventName);
     eventStatisticsDTO.setTotalAttendees(eventAttendees);
     eventStatisticsDTO.setTotalRevenue(totalRevenuePerEvent);
     eventStatisticsDTO.setDailyRevenue(dailyRevenue);
     eventStatisticsDTO.setMonthlyRevenue(monthlyRevenue);
     eventStatisticsDTO.setYearlyRevenue(yearlyRevenue);

     return eventStatisticsDTO;



    }
}
