package com.gelitix.backend.promoDetail.controller;


import com.gelitix.backend.auth.helpers.Claims;
import com.gelitix.backend.promoDetail.dto.CreatePromoDto;
import com.gelitix.backend.promoDetail.entity.PromoDetail;
import com.gelitix.backend.promoDetail.service.PromoDetailService;
import com.gelitix.backend.response.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/promo-detail")
public class PromoDetailController {
    private final PromoDetailService promoDetailService;

    public PromoDetailController(PromoDetailService promoDetailService) {
        this.promoDetailService = promoDetailService;
    }

}
