package com.gelitix.backend.promoDetail.service.impl;

import com.gelitix.backend.event.dto.EventDto;
import com.gelitix.backend.event.entity.Event;
import com.gelitix.backend.event.service.EventService;
import com.gelitix.backend.order.service.OrderService;
import com.gelitix.backend.promoDetail.dto.CreatePromoDto;
import com.gelitix.backend.promoDetail.entity.PromoDetail;
import com.gelitix.backend.promoDetail.repository.PromoDetailRepository;
import com.gelitix.backend.promoDetail.service.PromoDetailService;
import com.gelitix.backend.users.service.UserService;
import org.hibernate.service.spi.ServiceException;
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
    public CreatePromoDto addPromo(CreatePromoDto createPromoDto, String email) {
        Event currentEvent = eventService.getEventEntityById(createPromoDto.getEventId());
        String eventOrganizerEmail = currentEvent.getUser().getEmail();
        if (!eventOrganizerEmail.equals(email)) {
            throw new ServiceException("You are not allowed to add promo to this event");
        }
        PromoDetail promoDetail = new PromoDetail();
        if (Boolean.TRUE.equals(createPromoDto.getIsReferral())){
            promoDetail.setDiscount(new BigDecimal("0.1"));
        }
        promoDetail.setDiscount(createPromoDto.getDiscount());
        promoDetail.setName(createPromoDto.getName());
        promoDetail.setStartValid(createPromoDto.getStartValid());
        promoDetail.setEndValid(createPromoDto.getEndValid());
        promoDetail.setEvent(eventService.getEventEntityById(createPromoDto.getEventId()));
        promoDetail.setQuantity(createPromoDto.getQuantity());
        promoDetail.setIsReferral(createPromoDto.getIsReferral());
        promoDetailRepository.save(promoDetail);
        if (getPromoDetailsbyEventId(promoDetail.getId() )== null) {
            throw new ServiceException("Failed to add promo to this event");
        }
        return createPromoDto;
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
//        Event currentEvent = eventService.getEventById(eventId);
        EventDto currentEvent = eventService.getEventById(eventId);
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
//        Users currentUser = userService.getUserByEmail(email)
//                .orElseThrow(() -> new IllegalArgumentException("User not found for email: " + email));
//
//
//        Long eventOrganizerId = eventService.getEventById(id).getUserId();
//        String eventOrganizerEmail= userService.findById(eventOrganizerId).getEmail();
//
//        if (currentUser == null || eventOrganizerId == null) {
//            throw new IllegalArgumentException("User Cannot Be Found " + id);
//        }
//
//        if (!email.equals(eventOrganizerEmail)) {
//            throw new SecurityException("You do not have permission to delete this promo detail");
//        }
        PromoDetail currentPromo= promoDetailRepository.findById(id).get();
        currentPromo.setDeletedAt(Instant.now());

        return promoDetailRepository.save(currentPromo);
    }
}
