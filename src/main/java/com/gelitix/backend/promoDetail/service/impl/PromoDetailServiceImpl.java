package com.gelitix.backend.promoDetail.service.impl;

import com.gelitix.backend.event.entity.Event;
import com.gelitix.backend.order.entity.Order;
import com.gelitix.backend.promoDetail.dto.CreatePromoDto;
import com.gelitix.backend.promoDetail.entity.PromoDetail;
import com.gelitix.backend.promoDetail.repository.PromoDetailRepository;
import com.gelitix.backend.promoDetail.service.PromoDetailService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class PromoDetailServiceImpl implements PromoDetailService {
    private final PromoDetailRepository promoDetailRepository;

    public PromoDetailServiceImpl(PromoDetailRepository promoDetailRepository) {
        this.promoDetailRepository = promoDetailRepository;
    }


    @Override
    public Optional<PromoDetail> getPromoDetails(Long id) {
        return promoDetailRepository.findById(id);
    }

    @Override
    public PromoDetail addPromo(CreatePromoDto createPromoDto, Event event, Order order) {
        PromoDetail promoDetail = new PromoDetail();
        if (Boolean.TRUE.equals(createPromoDto.getIsReferral())){
            promoDetail.setDiscount(0.1);
        };
        promoDetail.setDiscount(createPromoDto.getDiscount());
        promoDetail.setName(createPromoDto.getName());
        promoDetail.setStartValid(createPromoDto.getStartValid());
        promoDetail.setEndValid(createPromoDto.getEndValid());
        promoDetail.setEvent(event);
        return promoDetailRepository.save(promoDetail);
    }

    public void deletePromo(Long id) {
        PromoDetail currentPromo= promoDetailRepository.findById(id).get();
        currentPromo.setDeletedAt(Instant.now());
    }
}
