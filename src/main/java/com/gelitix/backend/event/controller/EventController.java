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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/events")
@Validated
@Log
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventDto> getAllEvents() {
        return eventService.getAllEvents().stream()
                .map(eventService::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @RolesAllowed("ROLE_EVENT_ORGANIZER")
    @PostMapping("/create-event")
    public ResponseEntity<?> createEvent(@RequestBody EventDto eventDto) {
        String role = Claims.getRoleFromJwt();
        if (role == null || !role.equals(RoleName.ROLE_EVENT_ORGANIZER.name())) {
            return Response.failed("Unauthorized");
        }
        // Set the user ID from the authenticated user's claims
        Integer userId = Claims.getUserIdFromJwt().intValue(); // Convert Long to Integer
        eventDto.setUserId(userId);

        Event event = eventService.createEvent(eventDto);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable Integer id) {
        Event event = eventService.getEventById(id);
        if (event != null) {
            return Response.success(200, "Event Found", eventService.mapEntityToDto(event));
        } else {
            return Response.failed("Event Not Found");
        }
    }

    @RolesAllowed("ROLE_EVENT_ORGANIZER")
    @PutMapping("/{id}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable Integer id, @RequestBody EventDto eventDto) {
        Event updatedEvent = eventService.updateEvent(id, eventDto);
        return ResponseEntity.ok(eventService.mapEntityToDto(updatedEvent));
    }

    @RolesAllowed("ROLE_EVENT_ORGANIZER")
    @DeleteMapping("/delete-event/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable("id") Integer id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok("Event deleted successfully");
    }
}
