package com.gelitix.backend.event.service;

import com.gelitix.backend.event.dto.EventDto;
import com.gelitix.backend.event.dto.GetEventByIdResponseDto;
import com.gelitix.backend.event.entity.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;


import java.util.List;

public interface EventService {

    Event getEventById(Long id);

    Event createEvent(EventDto eventDTO);

    Page<Event> getAllEvents(String eventCategory, Pageable pageable);

    Event updateEvent(Long id, EventDto eventDto);

    void deleteEvent(Long id);

    EventDto mapEntityToDto(Event event);

    GetEventByIdResponseDto getEventByIdResponseDto (Long eventId);

    void mapDtoToEntity(EventDto eventDto, Event event);

}
