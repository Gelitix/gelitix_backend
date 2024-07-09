package com.gelitix.backend.order.repository;

import com.gelitix.backend.event.entity.Event;
import com.gelitix.backend.order.dao.EventAttendeeCountDao;
import com.gelitix.backend.order.dao.PeriodicalRevenueDao;
import com.gelitix.backend.order.entity.Order;
import com.gelitix.backend.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long>
{
    int countByEventId(Long eventId);

    Double countOrdersByUserId(Long userId);

    @Query("SELECT o.event as event, SUM(o.ticketQuantity) as attendeeCount FROM Order o WHERE o.event.id = :eventId")
    EventAttendeeCountDao countTotalAttendeesByEventId(@Param("eventId") Long eventId);

    @Query("SELECT COUNT(DISTINCT o.user) FROM Order o WHERE o.event.user.id = :userId")
    Double countUniqueCustomersByEventMaker(@Param("userId") Long userId);


    @Query("SELECT COUNT(o) > 0 FROM Order o " +
            "JOIN PromoDetail p ON p.id = o.promo.id " +
            "JOIN Users u ON u.id = o.user.id " +
            "WHERE u.id = :userId " +
            "AND p.isReferral = true " +
            "AND u.isReferred = true")
    boolean existsOrderWithReferralPromoForUser(@Param("userId") Long userId);


    @Query("SELECT SUM(o.finalPrice) FROM Order o WHERE o.event.user.id = :userId")
    BigDecimal countTotalRevenueByEventMaker(@Param("userId") Long userId);

    @Query(value = "SELECT DATE(o.created_at) as createdDate, SUM (o.final_price) as finalPrice " +
            "FROM orders o " +
            "JOIN event e ON o.event.id = e.id " +
            "JOIN users u ON e.user.id = u.id " +
            "WHERE u.id =: userId " +
            "GROUP BY DATE(o.created_at )", nativeQuery = true)
    List<PeriodicalRevenueDao> findDailyRevenueByEventMaker(@Param("eventId") Long eventId);


    @Query(value = "SELECT DATE_TRUNC('month', o.created_at) as createdMonth, SUM(o.final_price) as finalPrice " +
            "FROM orders o " +
            "JOIN event e ON o.event_id = e.id " +
            "JOIN users u ON e.user_id = u.id " +
            "WHERE u.id = :userId " +
            "GROUP BY DATE_TRUNC('month', o.created_at) " +
            "ORDER BY createdMonth",
            nativeQuery = true)
    List<PeriodicalRevenueDao> findMonthlyRevenueByEventMaker(@Param("userId") Long userId);


    @Query(value = "SELECT DATE_TRUNC('year', o.created_at) as createdYear, SUM(o.final_price) as finalPrice " +
            "FROM orders o " +
            "JOIN event e ON o.event_id = e.id " +
            "JOIN users u ON e.user_id = u.id " +
            "WHERE u.id = :userId " +
            "GROUP BY DATE_TRUNC('year', o.created_at) " +
            "ORDER BY createdYear",
            nativeQuery = true)
    List<PeriodicalRevenueDao> findYearlyRevenueByEventMaker(@Param("userId") Long userId);



}
