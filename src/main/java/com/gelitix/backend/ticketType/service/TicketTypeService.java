package com.gelitix.backend.ticketType.service;

import com.gelitix.backend.event.entity.Event;
import com.gelitix.backend.ticketType.dto.CreateTicketTypeDto;
import com.gelitix.backend.ticketType.entity.TicketType;

import java.util.List;
import java.util.Optional;

public interface TicketTypeService {
    Optional<TicketType> getTicketTypeById(Long id);
    List<TicketType> getAllTicketTypes();
    TicketType createTicketType(CreateTicketTypeDto createTicketTypeDto, Long eventId);
    void deleteTicketType(Long id);
    List<TicketType> getTicketTypesByEvent (Event event);
    int deductTicketQuantity(TicketType boughtTicketType, int orderedTicketQuantity);
}
