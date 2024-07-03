package com.gelitix.backend.event.service.impl;

import com.gelitix.backend.event.entity.Event;
import com.gelitix.backend.event.dto.EventDto;
import com.gelitix.backend.event.repository.EventRepository;
import com.gelitix.backend.event.service.EventService;
import com.gelitix.backend.eventLocation.entity.EventLocation;
import com.gelitix.backend.eventLocation.repository.EventLocationRepository;
import com.gelitix.backend.ticketType.entity.TicketType;
import com.gelitix.backend.ticketType.repository.TicketTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final EventLocationRepository eventLocationRepository;

    public EventServiceImpl(EventRepository eventRepository, TicketTypeRepository ticketTypeRepository, EventLocationRepository eventLocationRepository) {
        this.eventRepository = eventRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.eventLocationRepository = eventLocationRepository;
    }

    @Override
    @Transactional
    public Event createEvent(EventDto eventDto) {
        Event event = new Event();
        event.setName(eventDto.getName());

        // Convert java.sql.Timestamp to java.time.Instant
        event.setDate(eventDto.getDate());

        // Convert java.sql.Timestamp to java.time.LocalTime
        event.setStart(LocalTime.ofInstant(eventDto.getStartTime(), java.time.ZoneId.systemDefault()));
        event.setEnd(LocalTime.ofInstant(eventDto.getEndTime(), java.time.ZoneId.systemDefault()));

        event.setDescription(eventDto.getDescription());
        event.setIsFree(eventDto.getIsFree());


        EventLocation location = eventLocationRepository.findByName(eventDto.getLocation());
        if (location == null) {
            location = new EventLocation();
            location.setName(eventDto.getLocation());
            eventLocationRepository.save(location);
        }
        event.setLocation(location);

        event = eventRepository.save(event);

        final Event savedEvent = event;
        List<TicketType> ticketTypes = eventDto.getTicketTypes().stream().map(ticketTypeDto -> {
            TicketType ticketType = new TicketType();
            ticketType.setName(ticketTypeDto.getName());
            ticketType.setPrice(BigDecimal.valueOf(ticketTypeDto.getPrice()));
            ticketType.setQuantity(ticketTypeDto.getQuantity());
            ticketType.setEvent(savedEvent);
            return ticketType;
        }).collect(Collectors.toList());

        ticketTypeRepository.saveAll(ticketTypes);

        return event;
    }
}
