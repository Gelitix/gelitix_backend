package com.gelitix.backend.event.entity.service;


import com.gelitix.backend.event.entity.dto.EventDto;
import com.gelitix.backend.event.entity.entity.Event;

public interface EventService {
    Event createEvent(EventDto eventDTO);
}