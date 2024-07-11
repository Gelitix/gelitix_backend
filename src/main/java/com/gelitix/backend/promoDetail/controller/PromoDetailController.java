package com.gelitix.backend.promoDetail.controller;


import com.gelitix.backend.auth.helpers.Claims;
import com.gelitix.backend.promoDetail.dto.CreatePromoDto;
import com.gelitix.backend.promoDetail.entity.PromoDetail;
import com.gelitix.backend.promoDetail.service.PromoDetailService;
import com.gelitix.backend.response.Response;
import jakarta.annotation.security.RolesAllowed;
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

    @GetMapping("/{id}")
    public ResponseEntity<?> getPromosDetailsbyEventId(@RequestParam("eventId") Long eventId) {
        return Response.success(200, "This is the list:",promoDetailService.getPromoDetailsbyEventId(eventId));
    }

//    @GetMapping("/{userId}/{eventId}")
//    public ResponseEntity<?> getPromoDetailsByUserIdAndEventId(@RequestParam("userId") Long userId, Long eventId) {
//        return Response.success(200, "This is the list:",promoDetailService.getPromoDetailsbyUserIdAndEventId(userId, eventId));
//    }

    @GetMapping("/{userId}/{eventId}")
    public ResponseEntity<?> getPromoDetailsByUserIdAndEventId(@PathVariable Long userId, @PathVariable Long eventId) {
        return Response.success(200, "This is the list:", promoDetailService.getPromoDetailsbyUserIdAndEventId(userId, eventId));
    }

    @RolesAllowed("ROLE_EVENT_ORGANIZER")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePromoDetailsByEventId(@PathVariable("id") Long promoId) {
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        promoDetailService.deletePromoDetailsbyEventId(promoId, email);
        return  Response.success("Your Promo Has Been Deleted");
            }


    @RolesAllowed("ROLE_EVENT_ORGANIZER")
    @PostMapping("/create-promo")
    public ResponseEntity<?> addPromo(@RequestBody CreatePromoDto createPromoDto) {
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        return  Response.success(200, "Promo is created", promoDetailService.addPromo(createPromoDto,email));
    }
}
