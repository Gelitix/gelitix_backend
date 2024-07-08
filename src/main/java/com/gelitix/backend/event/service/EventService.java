package com.gelitix.backend.event.service;

import com.gelitix.backend.event.dto.EventDto;
import com.gelitix.backend.event.dto.GetEventByIdResponseDto;
import com.gelitix.backend.event.entity.Event;

import java.util.List;

public interface EventService {

    Event getEventById(Long id);

    Event createEvent(EventDto eventDTO);

    List<Event> getAllEvents();

    Event updateEvent(Long id, EventDto eventDto);

    void deleteEvent(Long id);

    EventDto mapEntityToDto(Event event);

    GetEventByIdResponseDto getEventByIdResponseDto (Long eventId);

}
