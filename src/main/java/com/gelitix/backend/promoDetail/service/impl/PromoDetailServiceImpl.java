package com.gelitix.backend.promoDetail.service.impl;

import com.gelitix.backend.event.entity.Event;
import com.gelitix.backend.event.service.EventService;
import com.gelitix.backend.order.entity.Order;
import com.gelitix.backend.order.service.OrderService;
import com.gelitix.backend.promoDetail.dto.CreatePromoDto;
import com.gelitix.backend.promoDetail.entity.PromoDetail;
import com.gelitix.backend.promoDetail.repository.PromoDetailRepository;
import com.gelitix.backend.promoDetail.service.PromoDetailService;
import com.gelitix.backend.response.Response;
import com.gelitix.backend.users.entity.Users;
import com.gelitix.backend.users.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PromoDetailServiceImpl implements PromoDetailService {
    private static final Logger log = LoggerFactory.getLogger(PromoDetailServiceImpl.class);
    private final PromoDetailRepository promoDetailRepository;
    private final EventService eventService;
    private final UserService userService;
    private final OrderService orderService;

    public PromoDetailServiceImpl(PromoDetailRepository promoDetailRepository, EventService eventService, UserService userService, @Lazy OrderService orderService) {
        this.promoDetailRepository = promoDetailRepository;
        this.eventService = eventService;
        this.userService = userService;
        this.orderService = orderService;
    }


    @Override
    public Optional<PromoDetail> getPromoDetails(Long id) {
        return promoDetailRepository.findById(id);
    }

    @Override
    public PromoDetail addPromo(CreatePromoDto createPromoDto, Event event, Order order) {
        PromoDetail promoDetail = new PromoDetail();
        if (Boolean.TRUE.equals(createPromoDto.getIsReferral())){
            promoDetail.setDiscount(new BigDecimal("0.1"));
        }
        promoDetail.setDiscount(createPromoDto.getDiscount());
        promoDetail.setName(createPromoDto.getName());
        promoDetail.setStartValid(createPromoDto.getStartValid());
        promoDetail.setEndValid(createPromoDto.getEndValid());
        promoDetail.setEvent(event);
        return promoDetailRepository.save(promoDetail);
    }

    @Override
    public List<PromoDetail> getPromoDetailsbyEventId(Long eventId) {
        if (promoDetailRepository.findPromoDetailsByEventId(eventId) == null) {
            throw new IllegalArgumentException("No promo detail found for event id " + eventId);
        }
        return  promoDetailRepository.findPromoDetailsByEventId(eventId);
        }


    @Override
    public List<PromoDetail> getPromoDetailsbyUserIdAndEventId(Long userId, Long eventId) {
        Event currentEvent = eventService.getEventById(eventId);
        if(currentEvent.getIsFree()){
            log.info("Event {} is free. No promo details applicable.", eventId);
            return Collections.emptyList();
        }
        List<PromoDetail> referralPromo = promoDetailRepository.findPromoDetailByIsReferralAndEventId(true, eventId);
        List<PromoDetail> regularPromo = promoDetailRepository.findPromoDetailByIsReferralAndEventId(false, eventId);
        if(orderService.existsOrderWithReferralPromoForUser(userId)){
            return regularPromo;
        }
        List<PromoDetail> allPromos = new ArrayList<>(referralPromo);
        allPromos.addAll(regularPromo);
        return allPromos;    }


    @Override
    public PromoDetail deletePromoDetailsbyEventId(Long id,String email) {
        Optional<Users> currentUserOpts = userService.getUserByEmail(email);
        Users currentUser = currentUserOpts.get();
        Users eventOrganizer = eventService.getEventById(id).getUser();

        if (currentUser == null || eventOrganizer == null) {
            throw new IllegalArgumentException("User Cannot Be Found " + id);
        }

        if (currentUser != eventOrganizer) {
            throw new SecurityException("You do not have permission to delete this promo detail");
        }
        PromoDetail currentPromo= promoDetailRepository.findById(id).get();
        currentPromo.setDeletedAt(Instant.now());

        return promoDetailRepository.save(currentPromo);
    }
}
