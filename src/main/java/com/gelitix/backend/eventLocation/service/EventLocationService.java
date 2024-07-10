package com.gelitix.backend.eventLocation.service;

import com.gelitix.backend.eventLocation.dto.EventLocationDto;
import com.gelitix.backend.eventLocation.entity.EventLocation;

import java.util.List;


public interface EventLocationService {
    List<EventLocationDto> getEventLocations();

    EventLocation findByName(String name);
}
