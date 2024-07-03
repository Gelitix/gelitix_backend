package com.gelitix.backend.promoDetail.service;

import com.gelitix.backend.event.entity.Event;
import com.gelitix.backend.order.entity.Order;
import com.gelitix.backend.promoDetail.dto.CreatePromoDto;
import com.gelitix.backend.promoDetail.entity.PromoDetail;

import java.util.Optional;

public interface PromoDetailService {
    Optional<PromoDetail> getPromoDetails(Long id);
    PromoDetail addPromo(CreatePromoDto createPromoDto, Event event, Order order);
}
