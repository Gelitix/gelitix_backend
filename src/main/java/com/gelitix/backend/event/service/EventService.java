package com.gelitix.backend.event.service;


import com.gelitix.backend.event.dto.EventDto;
import com.gelitix.backend.event.entity.Event;

public interface EventService {
    Event createEvent(EventDto eventDTO);
}