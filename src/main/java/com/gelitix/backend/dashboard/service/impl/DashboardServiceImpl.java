package com.gelitix.backend.dashboard.service.impl;

import com.gelitix.backend.dashboard.dto.EventStatisticsDTO;
import com.gelitix.backend.dashboard.service.DashboardService;
import com.gelitix.backend.event.service.EventService;
import com.gelitix.backend.order.dao.PeriodicalRevenueDao;
import com.gelitix.backend.order.service.OrderService;
import com.gelitix.backend.users.entity.Users;
import com.gelitix.backend.users.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class DashboardServiceImpl implements DashboardService {
    @Lazy
    private final OrderService orderService;
    private final EventService eventService;
    private final UserService userService;

    public DashboardServiceImpl(OrderService orderService, EventService eventService, UserService userService) {
        this.orderService = orderService;
        this.eventService = eventService;
        this.userService = userService;
    }

    @Override
    public EventStatisticsDTO getEventStatistics(String email) {
        Optional<Users> currentUseropts = userService.getUserByEmail(email);
        Users currentUser = currentUseropts.get();
        Long currentUserId =currentUser.getId();

        Long totalOrderByEventMaker = orderService.countOrdersByUserId(currentUserId);
        BigDecimal totalRevenueByEventMaker = orderService.countTotalRevenueByEvent(currentUserId);
        Long countUniqueCustomersByEventMaker = orderService.countUniqueCustomersByEventMaker(currentUserId);
        List<PeriodicalRevenueDao> dailyRevenue = orderService.findDailyRevenueByEventMaker(currentUserId);
        List<PeriodicalRevenueDao> monthlyRevenue = orderService.findMonthlyRevenueByEventMaker(currentUserId);
        List<PeriodicalRevenueDao> yearlyRevenue = orderService.findYearlyRevenueByEventMaker(currentUserId);


        EventStatisticsDTO eventStatisticsDTO = new EventStatisticsDTO();
        eventStatisticsDTO.setTotalCustomers(countUniqueCustomersByEventMaker);
        eventStatisticsDTO.setTotalOrders(totalOrderByEventMaker);
        eventStatisticsDTO.setTotalRevenue(totalRevenueByEventMaker);
        eventStatisticsDTO.setDailyRevenue(dailyRevenue);
        eventStatisticsDTO.setMonthlyRevenue(monthlyRevenue);
        eventStatisticsDTO.setYearlyRevenue(yearlyRevenue);

        return eventStatisticsDTO;



    }
}
