package com.gelitix.backend.ticketType.repository;

import com.gelitix.backend.ticketType.entity.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TicketTypeRepository extends JpaRepository <TicketType, Long> {
}
