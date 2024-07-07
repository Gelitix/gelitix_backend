package com.gelitix.backend.event.service.impl;

import com.gelitix.backend.event.dto.EventDto;
import com.gelitix.backend.event.entity.Event;
import com.gelitix.backend.event.repository.EventRepository;
import com.gelitix.backend.event.service.EventService;
import com.gelitix.backend.eventCategory.entity.EventCategory;
import com.gelitix.backend.eventCategory.repository.EventCategoryRepository;
import com.gelitix.backend.eventLocation.entity.EventLocation;
import com.gelitix.backend.eventLocation.repository.EventLocationRepository;
import com.gelitix.backend.ticketType.dto.TicketTypeDto;
import com.gelitix.backend.ticketType.entity.TicketType;
import com.gelitix.backend.ticketType.repository.TicketTypeRepository;
import com.gelitix.backend.users.dto.UserDto;
import com.gelitix.backend.users.entity.Users;
import com.gelitix.backend.users.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final EventLocationRepository eventLocationRepository;
    private final EventCategoryRepository eventCategoryRepository;
    private final UserService userService;

    public EventServiceImpl(EventRepository eventRepository, TicketTypeRepository ticketTypeRepository, EventLocationRepository eventLocationRepository, EventCategoryRepository eventCategoryRepository, UserService userService) {
        this.eventRepository = eventRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.eventLocationRepository = eventLocationRepository;
        this.eventCategoryRepository = eventCategoryRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public Event createEvent(EventDto eventDto) {
        Event event = new Event();
        mapDtoToEntity(eventDto, event);
        event.setCreatedAt(Instant.now());
        event.setUpdatedAt(Instant.now());
        event = eventRepository.save(event);

        System.out.println("Saved Event: " + event);
        saveTicketTypes(eventDto, event);

        return event;
    }

    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    @Override
    @Transactional
    public Event updateEvent(Integer id, EventDto eventDto) {
        Event existingEvent = eventRepository.findById(id).orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        mapDtoToEntity(eventDto, existingEvent);
        existingEvent.setUpdatedAt(Instant.now());
        existingEvent = eventRepository.save(existingEvent);

        // Update ticket types
        saveTicketTypes(eventDto, existingEvent);

        return existingEvent;
    }

    @Override
    public Event getEventById(Integer id) {
        return eventRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteEvent(Integer id) {
        eventRepository.deleteById(id);
    }

    private void mapDtoToEntity(EventDto eventDto, Event event) {
        event.setId(eventDto.getId());
        event.setName(eventDto.getName());
        event.setDate(eventDto.getDate());
        event.setStart(LocalTime.ofInstant(eventDto.getStartTime(), java.time.ZoneId.systemDefault()));
        event.setEnd(LocalTime.ofInstant(eventDto.getEndTime(), java.time.ZoneId.systemDefault()));
        event.setOrganizer(eventDto.getOrganizer());
        event.setDescription(eventDto.getDescription());
        EventCategory eventCategory = eventCategoryRepository.findByName(eventDto.getEventCategory());
        event.setEventCategory(eventCategory);
        event.setIsFree(eventDto.getIsFree());

        EventLocation location = eventLocationRepository.findByName(eventDto.getLocation());
        event.setLocation(location);
        Users user = userService.findById(eventDto.getUserId()); // Fetch the user using UserService
        event.setUser(user);
    }

    @Override
    public EventDto mapEntityToDto(Event event) {
        EventDto eventDto = new EventDto();
        eventDto.setId(event.getId());
        eventDto.setName(event.getName());
        eventDto.setDate(event.getDate());
        eventDto.setStartTime(event.getStart().atDate(LocalDate.now()).toInstant(ZoneOffset.UTC));
        eventDto.setEndTime(event.getEnd().atDate(LocalDate.now()).toInstant(ZoneOffset.UTC));
        eventDto.setDescription(event.getDescription());
        eventDto.setOrganizer(event.getOrganizer());
        eventDto.setLocation(event.getLocation().getName());

        eventDto.setEventCategory(event.getEventCategory().getName());
        eventDto.setIsFree(event.getIsFree());
        eventDto.setUserId(event.getUser().getId());
        return eventDto;

    }

    private void saveTicketTypes(EventDto eventDto, Event event) {
        List<TicketType> ticketTypes = eventDto.getTicketTypes().stream().map(ticketTypeDto -> {
            TicketType ticketType = new TicketType();
            ticketType.setName(ticketTypeDto.getName());
            ticketType.setPrice(ticketTypeDto.getPrice());
            ticketType.setQuantity(ticketTypeDto.getQuantity());
            ticketType.setEvent(event);
            return ticketType;
        }).collect(Collectors.toList());

        ticketTypeRepository.saveAll(ticketTypes);
    }

    @Override
    public Event findEventById(long id) {
        if (eventRepository.findById(id).isEmpty())
        {throw new IllegalArgumentException("No event found with id " + id);}
        return eventRepository.findById(id).orElse(null);
    }


}
