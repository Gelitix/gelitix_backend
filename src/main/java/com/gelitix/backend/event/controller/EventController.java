package com.gelitix.backend.event.controller;

import com.gelitix.backend.auth.helpers.Claims;
import com.gelitix.backend.event.dto.EventDto;
import com.gelitix.backend.event.entity.Event;
import com.gelitix.backend.event.service.EventService;
import com.gelitix.backend.response.Response;
import com.gelitix.backend.users.entity.RoleName;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/events")
@Validated
@Log
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @RolesAllowed("ROLE_EVENT_ORGANIZER")
    @PostMapping("/create-event")
//    @PreAuthorize("hasRole('ROLE_EVENT_ORGANIZER')")
    public ResponseEntity<?> createEvent(@RequestBody EventDto eventDto) {
        String role = Claims.getRoleFromJwt();
        if (role == null || !role.equals(RoleName.ROLE_EVENT_ORGANIZER.name())) {
            return Response.failed("Unauthorized");
        }
        Event event = eventService.createEvent(eventDto);
        return ResponseEntity.ok(event);
    }
}
