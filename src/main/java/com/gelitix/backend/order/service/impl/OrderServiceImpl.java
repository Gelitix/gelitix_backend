package com.gelitix.backend.order.service.impl;

import com.gelitix.backend.event.entity.Event;
import com.gelitix.backend.event.repository.EventRepository;
import com.gelitix.backend.event.service.EventService;
import com.gelitix.backend.order.dao.EventAttendeeCountDao;
import com.gelitix.backend.order.dao.PeriodicalRevenueDao;
import com.gelitix.backend.order.dto.CreateOrderRequestDto;
import com.gelitix.backend.order.dto.CreateOrderResponseDto;
import com.gelitix.backend.order.entity.Order;
import com.gelitix.backend.order.repository.OrderRepository;
import com.gelitix.backend.order.service.OrderService;
import com.gelitix.backend.point.service.PointService;
import com.gelitix.backend.promoDetail.entity.PromoDetail;
import com.gelitix.backend.promoDetail.service.PromoDetailService;
import com.gelitix.backend.ticketType.entity.TicketType;
import com.gelitix.backend.ticketType.service.TicketTypeService;
import com.gelitix.backend.users.entity.Users;
import com.gelitix.backend.users.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final EventService eventService;
    private final PromoDetailService promoDetailService;
    private final TicketTypeService ticketTypeService;
    private final PointService pointService;

    public OrderServiceImpl(OrderRepository orderRepository, UserService userService, EventService eventService, EventRepository eventRepository, @Lazy PromoDetailService promoDetailService, TicketTypeService ticketTypeService, PointService pointService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.eventService = eventService;
        this.promoDetailService = promoDetailService;
        this.ticketTypeService = ticketTypeService;
        this.pointService = pointService;
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
        return orderRepository.existsOrderWithReferralPromoForUser(userId);
    }

    @Override
    public CreateOrderResponseDto createOrder(CreateOrderRequestDto createOrderRequestDto, String email) {

        Order newOrder = new Order();

        Optional<Users> currentUserOpts =userService.getUserByEmail(email);
        Users currentUser = currentUserOpts.get();
        Long currentUserId = currentUser.getId();
        newOrder.setUser(userService.findById(currentUserId));

        Event chosenEvent = eventService.getEventEntityById(createOrderRequestDto.getEventId());
        if(chosenEvent == null){
            throw new IllegalArgumentException("Event not found with id: " + createOrderRequestDto.getEventId());
        }
//        if(chosenEvent.getDate().isAfter(Instant.now())){throw new IllegalArgumentException("This event has not started yet");};
        newOrder.setEvent(eventService.getEventEntityById(createOrderRequestDto.getEventId()));

        newOrder.setFullName(createOrderRequestDto.getFullName());

        newOrder.setEmail(createOrderRequestDto.getEmail());

        newOrder.setCreatedAt(Instant.now());

        newOrder.setIdentityCard(createOrderRequestDto.getIdentityCard());

        newOrder.setPhoneNumber(createOrderRequestDto.getPhoneNumber());

        Optional<TicketType> chosenTicketTypeOpts = ticketTypeService.getTicketTypeById(createOrderRequestDto.getTicketTypeId());
        TicketType chosenTicketType = chosenTicketTypeOpts.get();
        newOrder.setTicketType(chosenTicketType);


        newOrder.setTicketQuantity(createOrderRequestDto.getTicketQuantity());
        if (newOrder.getTicketQuantity() > chosenTicketType.getQuantity()) {
            throw new IllegalArgumentException("Ticket quantity exceeds available quantity.");
        }
        ticketTypeService.deductTicketQuantity(chosenTicketType, createOrderRequestDto.getTicketQuantity());

        BigDecimal price = BigDecimal.valueOf(newOrder.getTicketQuantity()).multiply(chosenTicketType.getPrice());
        BigDecimal discountPercentage = BigDecimal.ZERO;
        if (createOrderRequestDto.getPromoId() != null){
            PromoDetail chosenPromoDetail = promoDetailService.getPromoDetails(createOrderRequestDto.getPromoId()).orElseThrow(()-> new RuntimeException("Promo Doesn't Exist"));
            if (chosenPromoDetail.getQuantity() == 0){
                throw new IllegalArgumentException("Promo is already over");
            }
            newOrder.setPromo(chosenPromoDetail);
            chosenPromoDetail.setQuantity(chosenPromoDetail.getQuantity()-1);
            discountPercentage = newOrder.getPromo().getDiscount();
        } else newOrder.setFinalPrice(price);

        BigDecimal discount = discountPercentage.multiply(price) ;
        BigDecimal discountPrice = price.subtract(discount);

        if (createOrderRequestDto.getPointUsed() != null && createOrderRequestDto.getPointUsed().compareTo(currentUser.getPointBalance()) <= 0) {
            newOrder.setFinalPrice(discountPrice.subtract(createOrderRequestDto.getPointUsed()));
            pointService.updateUserPointBalance(currentUserId,createOrderRequestDto.getPointUsed());
            pointService.deductPointHistory(currentUser, createOrderRequestDto.getPointUsed());
            userService.deductPointBalance(currentUserId, createOrderRequestDto.getPointUsed());
        }else newOrder.setFinalPrice(discountPrice);

        orderRepository.save(newOrder);

        pointService.updateUserPointBalance(currentUserId,createOrderRequestDto.getPointUsed());

        int orderedTicketQuantity = newOrder.getTicketQuantity();
        ticketTypeService.deductTicketQuantity(chosenTicketType, orderedTicketQuantity);

        CreateOrderResponseDto createOrderResponse= new CreateOrderResponseDto();
        createOrderResponse.setOrderId(newOrder.getId());
        createOrderResponse.setUserName(newOrder.getUser().getUsername());
        createOrderResponse.setEventName(newOrder.getEvent().getName());
        createOrderResponse.setPromo(newOrder.getPromo() != null ? newOrder.getPromo().getName() : null);
        createOrderResponse.setTicketType(newOrder.getTicketType().getName());
        createOrderResponse.setTicketQuantity(newOrder.getTicketQuantity());
        createOrderResponse.setDiscountPercentage(discountPercentage);
        createOrderResponse.setDiscountPrice(discount);
        createOrderResponse.setFinalPrice(newOrder.getFinalPrice());
        return createOrderResponse;
    }

    @Override
    public EventAttendeeCountDao countAttendeesByEventId(Long eventId) {
        return orderRepository.countTotalAttendeesByEventId(eventId);
    }

    @Override
    public BigDecimal countTotalRevenueByEvent(Long userId) {
        Users currentUser = userService.findById(userId);
        if (currentUser == null) {
            throw new IllegalArgumentException("Event not found with id: " + userId);
        }
        return orderRepository.countTotalRevenueByEventMaker(userId);
    }

    @Override
    public List<PeriodicalRevenueDao> findDailyRevenueByEventMaker(Long userId) {
        if(eventService.getEventById(userId) == null){
            throw new IllegalArgumentException("Event not found for id: " + userId);
        }
        return orderRepository.findDailyRevenueByEventMaker(userId);
    }

    @Override
    public List<PeriodicalRevenueDao> findMonthlyRevenueByEventMaker(Long eventId) {
        if(eventService.getEventById(eventId) == null){
            throw new IllegalArgumentException("Event not found for id: " + eventId);
        }
        return orderRepository.findMonthlyRevenueByEventMaker(eventId);
    }

    @Override
    public List<PeriodicalRevenueDao> findYearlyRevenueByEventMaker(Long eventId) {
        if(eventService.getEventById(eventId) == null){
            throw new IllegalArgumentException("Event not found for id: " + eventId);
        }
        return orderRepository.findYearlyRevenueByEventMaker(eventId);
    }

    @Override
    public Long countOrdersByUserId(Long userId) {
        return orderRepository.countByUserId(userId);
    }

    @Override
    public Long countUniqueCustomersByEventMaker(Long userId) {
        return orderRepository.countUniqueCustomersByEventMaker(userId);
    }

    @Override
    public List<Order> findOrdersByUserId(Long userId) {
        return orderRepository.findOrdersByUserId(userId);
    }

}
