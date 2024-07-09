package com.gelitix.backend.dashboard.controller;

import com.gelitix.backend.auth.helpers.Claims;
import com.gelitix.backend.dashboard.service.DashboardService;
import com.gelitix.backend.response.Response;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @RolesAllowed("ROLE_EVENT_ORGANIZER")
    @GetMapping("/{eventId}")
    public ResponseEntity<?> getEventStatistics() {
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        return Response.success(200, "This is the statistics data",dashboardService.getEventStatistics(email));
    }

}
