package com.gelitix.backend.ticketType.repository;

import com.gelitix.backend.event.entity.Event;
import com.gelitix.backend.ticketType.entity.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface TicketTypeRepository extends JpaRepository <TicketType, Long> {
 TicketType findByEvent(Event event);
}
