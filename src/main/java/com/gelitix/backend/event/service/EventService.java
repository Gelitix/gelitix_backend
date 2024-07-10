package com.gelitix.backend.event.service;

import com.gelitix.backend.event.dto.EventDto;
import com.gelitix.backend.event.dto.EventResponseDto;
import com.gelitix.backend.event.dto.GetEventByIdResponseDto;
import com.gelitix.backend.event.entity.Event;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;


import java.io.IOException;
import java.util.List;

public interface EventService {

    EventDto getEventById(Long id);

    EventDto createEvent(EventDto eventDTO);

//    List<EventDto> getAllEvents();

    Page<Event> getAllEvents(String eventCategory, Pageable pageable, String order, String sort);

    EventDto updateEvent(Long id, EventDto eventDto);

    void deleteEvent(Long id);

    EventDto mapEntityToDto(Event event);

    Event getEventEntityById(Long id);

    void mapDtoToEntity(EventDto eventDto, Event event);

}
