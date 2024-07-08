package com.gelitix.backend.ticketType.repository;

import com.gelitix.backend.event.entity.Event;
import com.gelitix.backend.ticketType.entity.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketTypeRepository extends JpaRepository <TicketType, Long> {
    List<TicketType> findByEventId(long eventId);
    void deleteByEventId(Long id);
}
