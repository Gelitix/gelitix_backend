package com.gelitix.backend.event.service;

import com.gelitix.backend.event.dto.EventDto;
import com.gelitix.backend.event.entity.Event;

import java.util.List;

public interface EventService {

    Event getEventById(Integer id);

    Event createEvent(EventDto eventDTO);

    List<Event> getAllEvents();

    Event updateEvent(Integer id, EventDto eventDto);

    void deleteEvent(Integer id);

    EventDto mapEntityToDto(Event event);


}
