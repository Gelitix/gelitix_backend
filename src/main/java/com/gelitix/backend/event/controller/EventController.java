package com.gelitix.backend.event.controller;

import com.gelitix.backend.auth.helpers.Claims;
import com.gelitix.backend.event.dto.EventDto;
import com.gelitix.backend.event.dto.GetEventByIdResponseDto;
import com.gelitix.backend.event.entity.Event;
import com.gelitix.backend.event.service.EventService;
import com.gelitix.backend.response.Response;
import com.gelitix.backend.users.entity.RoleName;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.java.Log;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;


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
    public Page<EventDto> getAllEvents( @RequestParam(required = false) String eventCategory,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        Page<Event> eventPage = eventService.getAllEvents(eventCategory, PageRequest.of(page, size));
        return eventPage.map(eventService::mapEntityToDto);
    }

    @RolesAllowed("ROLE_EVENT_ORGANIZER")
    @PostMapping(value = "/create-event", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EventDto> createEvent(@ModelAttribute EventDto eventDto,
                                                @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        String role = Claims.getRoleFromJwt();
        if (role == null || !role.equals(RoleName.ROLE_EVENT_ORGANIZER.name())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = Claims.getUserIdFromJwt();
        eventDto.setUserId(userId);
        eventDto.setImageFile(imageFile);

        EventDto createdEvent = eventService.createEvent(eventDto);
        return ResponseEntity.ok(createdEvent);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDto> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(eventService.getEventById(id));
    }

    @RolesAllowed("ROLE_EVENT_ORGANIZER")
    @PutMapping("/{id}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable Long id, @ModelAttribute EventDto eventDto,
                                                @RequestParam(value = "image", required = false) MultipartFile imageFile) {

        Long userId = Claims.getUserIdFromJwt();
        EventDto existingEvent = eventService.getEventById(id);
        eventDto.setImageFile(imageFile);
        EventDto updatedEvent = eventService.updateEvent(id, eventDto);
        return ResponseEntity.ok(updatedEvent);
    }

    @RolesAllowed("ROLE_EVENT_ORGANIZER")
    @DeleteMapping("/delete-event/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable("id") Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok("Event deleted successfully");
    }

//    @GetMapping("/promo/{id}")
//    public ResponseEntity<?> getEventByIdResponseDto(@PathVariable Long id) {
//        return Response.success(200, "Event Found", eventService.getEventByIdResponseDto(id));
//
//    }
}
