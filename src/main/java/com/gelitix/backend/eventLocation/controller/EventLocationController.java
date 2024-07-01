package com.gelitix.backend.eventLocation.controller;

import com.gelitix.backend.eventLocation.entity.EventLocation;
import com.gelitix.backend.eventLocation.service.EventLocationService;
import com.gelitix.backend.response.Response;
import com.gelitix.backend.users.service.impl.UserServiceImpl;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Data
@RequestMapping("/api/v1/EventLocation")
public class EventLocationController {
    private final EventLocationService eventLocationService;
    private final UserServiceImpl userServiceImpl;

    public EventLocationController(EventLocationService eventLocationService, UserServiceImpl userServiceImpl) {
        this.eventLocationService = eventLocationService;
        this.userServiceImpl = userServiceImpl;
    }

    @GetMapping
    public List<EventLocation> getEventLocations() {
        return eventLocationService.getEventLocations();
    }

}
