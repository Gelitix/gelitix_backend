package com.gelitix.backend.event.repository;

import com.gelitix.backend.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Long > {
}
