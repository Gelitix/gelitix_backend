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
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<?> getAllEvents( @RequestParam(required = false) String eventCategory,
                                        @RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(required = false, defaultValue = "id") String sort,
                                        @RequestParam(required = false, defaultValue = "asc") String order) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Event> eventPage = eventService.getAllEvents(eventCategory, pageable, order, sort);
        Page<EventDto> eventDtoPage = eventPage.map(eventService::mapEntityToDto);
        return Response.success(200, "OK", eventDtoPage);
    }

//    @RolesAllowed("ROLE_EVENT_ORGANIZER")
//    @PostMapping("/create-event")
//    public ResponseEntity<?> createEvent(@RequestBody EventDto eventDto) {
//        String role = Claims.getRoleFromJwt();
//        if (role == null || !role.equals(RoleName.ROLE_EVENT_ORGANIZER.name())) {
//            return Response.failed("Unauthorized");
//        }
//        // Set the user ID from the authenticated user's claims
//        Long userId = Claims.getUserIdFromJwt();
//        eventDto.setUserId(userId);
//
//        Event event = eventService.createEvent(eventDto);
//        return ResponseEntity.ok(event);
//    }

//    @RolesAllowed("ROLE_EVENT_ORGANIZER")
//    @PostMapping(value = "/create-event", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?> createEvent(@RequestPart("eventDto") EventDto eventDto,
//                                         @RequestPart(value = "image", required = false) MultipartFile imageFile) {
//        String role = Claims.getRoleFromJwt();
//        if (role == null || !role.equals(RoleName.ROLE_EVENT_ORGANIZER.name())) {
//            return Response.failed("Unauthorized");
//        }
//
//        Long userId = Claims.getUserIdFromJwt();
//        eventDto.setUserId(userId);
//
//        Event event = eventService.createEvent(eventDto);
//        return ResponseEntity.ok(event);
//    }

    @RolesAllowed("ROLE_EVENT_ORGANIZER")
    @PostMapping(value = "/create-event", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createEvent(@RequestPart("eventDto") EventDto eventDto,
                                         @RequestPart(value = "image", required = false) MultipartFile imageFile) {
        String role = Claims.getRoleFromJwt();
        if (role == null || !role.equals(RoleName.ROLE_EVENT_ORGANIZER.name())) {
            return Response.failed("Unauthorized");
        }

        Long userId = Claims.getUserIdFromJwt();
        eventDto.setUserId(userId);

        // Set the image file in the EventDto
        eventDto.setImageUrl(imageFile);

        Event event = eventService.createEvent(eventDto);
        return ResponseEntity.ok(event);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<GetEventByIdResponseDto>> getEventByIdResponseDto(@PathVariable Long id) {
        Event event = eventService.getEventById(id);
        if (event != null) {
            return Response.success(200, "Event Found", eventService.getEventByIdResponseDto(id));
        } else {
            return Response.failed("Event Not Found");
        }
    }

    @RolesAllowed("ROLE_EVENT_ORGANIZER")
    @PutMapping("/{id}")
    public ResponseEntity<EventDto> updateEvent(@PathVariable Long id, @RequestBody EventDto eventDto) {
        Event updatedEvent = eventService.updateEvent(id, eventDto);
        return ResponseEntity.ok(eventService.mapEntityToDto(updatedEvent));
    }

    @RolesAllowed("ROLE_EVENT_ORGANIZER")
    @DeleteMapping("/delete-event/{id}")
    public ResponseEntity<?> deleteEvent(@PathVariable("id") Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.ok("Event deleted successfully");
    }
}
