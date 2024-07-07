package com.gelitix.backend.order.service.impl;

import com.gelitix.backend.event.entity.Event;
import com.gelitix.backend.event.repository.EventRepository;
import com.gelitix.backend.event.service.EventService;
import com.gelitix.backend.order.dao.EventAttendeeCountDao;
import com.gelitix.backend.order.dao.PeriodicalRevenueDao;
import com.gelitix.backend.order.dto.CreateOrderDto;
import com.gelitix.backend.order.entity.Order;
import com.gelitix.backend.order.repository.OrderRepository;
import com.gelitix.backend.order.service.OrderService;
import com.gelitix.backend.promoDetail.entity.PromoDetail;
import com.gelitix.backend.promoDetail.service.PromoDetailService;
import com.gelitix.backend.ticketType.entity.TicketType;
import com.gelitix.backend.ticketType.service.TicketTypeService;
import com.gelitix.backend.users.entity.Users;
import com.gelitix.backend.users.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final EventService eventService;
    private final PromoDetailService promoDetailService;
    private final TicketTypeService ticketTypeService;

    public OrderServiceImpl(OrderRepository orderRepository, UserService userService, EventService eventService, EventRepository eventRepository,   @Lazy PromoDetailService promoDetailService, TicketTypeService ticketTypeService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.eventService = eventService;
        this.promoDetailService = promoDetailService;
        this.ticketTypeService = ticketTypeService;
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Order not found with id: " + id));
    }

    @Override
    public boolean existsOrderWithReferralPromoForUser(Long userId) {
        if(promoDetailService.getPromoDetails(userId).isEmpty()){
            throw new IllegalArgumentException("User Cannot Be Found " + userId);
        }
        return existsOrderWithReferralPromoForUser(userId);
    }

    @Override
    public Order createOrder(CreateOrderDto createOrderDto, String email) {

        Order newOrder = new Order();

        Optional<Users> currentUserOpts =userService.getUserByEmail(email);
        Users currentUser = currentUserOpts.get();
        Long currentUserId = Long.valueOf(currentUser.getId());
        newOrder.setUser(userService.findById(currentUserId));

        newOrder.setEvent(eventService.findEventById(createOrderDto.getEventId()));

        var discountPercentage= 0D;
        if (createOrderDto.getPromoId() != null){
        PromoDetail chosenPromoDetail = promoDetailService.getPromoDetails(createOrderDto.getPromoTypeId()).orElseThrow(()-> new RuntimeException("Promo Doesn't Exist"));
        newOrder.setPromo(chosenPromoDetail);
            discountPercentage = newOrder.getPromo().getDiscount();
        }

        Optional<TicketType> chosenTicketTypeOpts = ticketTypeService.getTicketTypeById(createOrderDto.getTicketTypeId());
        TicketType chosenTicketType = chosenTicketTypeOpts.get();
        newOrder.setTicketType(chosenTicketType);


        newOrder.setTicketQuantity(createOrderDto.getTicketQuantity());
        if (newOrder.getTicketQuantity() < chosenTicketType.getQuantity()) {
            throw new IllegalArgumentException("Ticket quantity exceeds available quantity.");
        }
        ticketTypeService.deductTicketQuantity(chosenTicketType, createOrderDto.getTicketQuantity());

        Double discount = discountPercentage * chosenTicketType.getPrice();
        newOrder.setFinal_price(BigDecimal.valueOf(chosenTicketType.getPrice()-discount));

        orderRepository.save(newOrder);

        int orderedTicketQuantity = newOrder.getTicketQuantity();
        ticketTypeService.deductTicketQuantity(chosenTicketType, orderedTicketQuantity);


        return newOrder;
    }

    @Override
    public EventAttendeeCountDao countAttendeesByEventId(Long eventId) {
        return orderRepository.countTotalAttendeesByEventId(eventId);
    }

    @Override
    public int countTotalRevenueByEvent(Long eventId) {
        Event currentEvent = eventService.findEventById(eventId);
        if (currentEvent == null) {
            throw new IllegalArgumentException("Event not found with id: " + eventId);
        }
        return orderRepository.countTotalRevenueByEvent(currentEvent);
    }

    @Override
    public List<PeriodicalRevenueDao> findDailyRevenueByEventId(Long eventId) {
        if(eventService.findEventById(eventId) == null){
            throw new IllegalArgumentException("Event not found for id: " + eventId);
        }
        return findDailyRevenueByEventId(eventId);
    }

    @Override
    public List<PeriodicalRevenueDao> findMonthlyRevenueByEventId(Long eventId) {
        if(eventService.findEventById(eventId) == null){
            throw new IllegalArgumentException("Event not found for id: " + eventId);
        }
        return findMonthlyRevenueByEventId(eventId);
    }

    @Override
    public List<PeriodicalRevenueDao> findYearlyRevenueByEventId(Long eventId) {
        if(eventService.findEventById(eventId) == null){
            throw new IllegalArgumentException("Event not found for id: " + eventId);
        }
        return findYearlyRevenueByEventId(eventId);
    }

}
