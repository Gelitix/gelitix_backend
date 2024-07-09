package com.gelitix.backend.event.service;

import com.gelitix.backend.event.dto.EventDto;
import com.gelitix.backend.event.dto.EventResponseDto;
import com.gelitix.backend.event.dto.GetEventByIdResponseDto;
import com.gelitix.backend.event.entity.Event;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface EventService {

    EventDto getEventById(Long id);

    EventDto createEvent(EventDto eventDTO);

    List<EventDto> getAllEvents();

    EventDto updateEvent(Long id, EventDto eventDto);

    void deleteEvent(Long id);

    EventDto mapEntityToDto(Event event);

    Event getEventEntityById(Long id);

//    GetEventByIdResponseDto getEventByIdResponseDto (Long eventId);

}

