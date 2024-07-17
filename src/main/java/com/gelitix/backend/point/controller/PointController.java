package com.gelitix.backend.point.controller;


import com.gelitix.backend.auth.helpers.Claims;
import com.gelitix.backend.point.entity.Point;
import com.gelitix.backend.point.service.PointService;
import com.gelitix.backend.response.Response;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Data
@RequestMapping("/api/v1/point")
public class PointController {
    private final PointService pointService;

    public PointController(PointService pointService, PointService pointService1) {
        this.pointService = pointService1;
    }

    @GetMapping("")
    public ResponseEntity<?> getPoints() {
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        return Response.success(200, "This is your point", pointService.getPoint(email) );
    };


  }
