package com.gelitix.backend.promoDetail.service.impl;

import com.gelitix.backend.promoDetail.repository.PromoDetailRepository;
import com.gelitix.backend.promoDetail.service.PromoDetailService;
import org.springframework.stereotype.Service;

@Service
public class PromoDetailServiceImpl implements PromoDetailService {
    private final PromoDetailRepository promoDetailRepository;

    public PromoDetailServiceImpl(PromoDetailRepository promoDetailRepository) {
        this.promoDetailRepository = promoDetailRepository;
    }


}
