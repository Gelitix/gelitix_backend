package com.gelitix.backend.eventLocation.controller;

import com.gelitix.backend.eventLocation.dto.EventLocationDto;
import com.gelitix.backend.eventLocation.entity.EventLocation;
import com.gelitix.backend.eventLocation.service.EventLocationService;

import com.gelitix.backend.users.service.impl.UserServiceImpl;
import lombok.Data;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Data
@RequestMapping("/api/v1/location")
public class EventLocationController {
    private final EventLocationService eventLocationService;

    public EventLocationController(EventLocationService eventLocationService, UserServiceImpl userServiceImpl) {
        this.eventLocationService = eventLocationService;
    }

    @GetMapping
    public List<EventLocationDto> getEventLocations() {
        return eventLocationService.getEventLocations();
    }

}
