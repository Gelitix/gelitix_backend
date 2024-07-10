package com.gelitix.backend.order.service;

import com.gelitix.backend.order.dao.EventAttendeeCountDao;
import com.gelitix.backend.order.dao.PeriodicalRevenueDao;
import com.gelitix.backend.order.dto.CreateOrderRequestDto;
import com.gelitix.backend.order.dto.CreateOrderResponseDto;
import com.gelitix.backend.order.entity.Order;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    Order getOrderById(Long id);
    boolean existsOrderWithReferralPromoForUser(Long userId);
    CreateOrderResponseDto createOrder(CreateOrderRequestDto createOrderRequestDto, String email);
    EventAttendeeCountDao countAttendeesByEventId(Long eventId);
    BigDecimal countTotalRevenueByEvent(Long userId);
    List<PeriodicalRevenueDao> findDailyRevenueByEventMaker(Long userId);
    List<PeriodicalRevenueDao> findMonthlyRevenueByEventMaker(Long userId);
    List<PeriodicalRevenueDao> findYearlyRevenueByEventMaker(Long userId);
    Long countOrdersByUserId(Long userId);
    Long countUniqueCustomersByEventMaker(Long userId);
    List<Order> findOrdersByUserId(Long userId);
}
