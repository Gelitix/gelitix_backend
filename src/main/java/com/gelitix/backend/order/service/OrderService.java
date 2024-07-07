package com.gelitix.backend.order.service;

import com.gelitix.backend.order.dao.EventAttendeeCountDao;
import com.gelitix.backend.order.dao.PeriodicalRevenueDao;
import com.gelitix.backend.order.dto.CreateOrderDto;
import com.gelitix.backend.order.entity.Order;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderService {
    Order getOrderById(Long id);
    boolean existsOrderWithReferralPromoForUser(Long userId);
    Order createOrder(CreateOrderDto createOrderDto, String email);
    EventAttendeeCountDao countAttendeesByEventId(Long eventId);
    int countTotalRevenueByEvent(Long eventId);
    List<PeriodicalRevenueDao> findDailyRevenueByEventId(Long eventId);
    List<PeriodicalRevenueDao> findMonthlyRevenueByEventId(Long eventId);
    List<PeriodicalRevenueDao> findYearlyRevenueByEventId(Long eventId);

}
