package com.gelitix.backend.ticketType.service.impl;

import com.gelitix.backend.event.entity.Event;
import com.gelitix.backend.event.service.EventService;
import com.gelitix.backend.ticketType.dto.CreateTicketTypeDto;
import com.gelitix.backend.ticketType.entity.TicketType;
import com.gelitix.backend.ticketType.repository.TicketTypeRepository;
import com.gelitix.backend.ticketType.service.TicketTypeService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class TicketTypeServiceImpl implements TicketTypeService {
    private final TicketTypeRepository ticketTypeRepository;
    private final EventService eventService;

    public TicketTypeServiceImpl(TicketTypeRepository ticketTypeRepository, @Lazy EventService eventService) {
        this.ticketTypeRepository = ticketTypeRepository;
        this.eventService = eventService;
    }


    @Override
    public Optional<TicketType> getTicketTypeById(Long id) {
        return ticketTypeRepository.findById(id);
    }

    @Override
    public List<TicketType> getAllTicketTypes() {
        return ticketTypeRepository.findAll();
    }

    @Override
    public TicketType createTicketType(CreateTicketTypeDto createTicketTypeDto, Long eventId) {
        Event currentEvent = eventService.getEventById(eventId);
        if (currentEvent == null) {throw new IllegalArgumentException("Event not found");}
        TicketType ticketType = new TicketType();
        ticketType.setEvent(currentEvent);
        ticketType.setName(createTicketTypeDto.getName());
        ticketType.setQuantity(createTicketTypeDto.getQuantity());
        if (currentEvent.getIsFree()) {
            ticketType.setPrice(BigDecimal.ZERO);
        }
        ticketType.setPrice(createTicketTypeDto.getPrice());

        return ticketTypeRepository.save(ticketType);
    }

    @Override
    public void deleteTicketType(Long id) {
        if (!ticketTypeRepository.existsById(id)) {throw new IllegalArgumentException("Ticket type not found");}
        TicketType currentTicketType = ticketTypeRepository.findById(id).get();
        currentTicketType.setDeletedAt(Instant.now());}

    @Override
    public TicketType getTicketTypesByEvent(Event event) {
     if (ticketTypeRepository.findByEvent(event) == null) {
            throw new IllegalArgumentException("Ticket types for this event cannot be found");
        }
        return ticketTypeRepository.findByEvent(event);
    }

    @Override
    public int deductTicketQuantity(TicketType boughtTicketType, int orderedTicketQuantity) {
        boughtTicketType.setQuantity(boughtTicketType.getQuantity() - orderedTicketQuantity);
        if (boughtTicketType.getQuantity() <= 0) {
            boughtTicketType.setQuantity(0);
        }
        return ticketTypeRepository.save(boughtTicketType).getQuantity();
    }
}


