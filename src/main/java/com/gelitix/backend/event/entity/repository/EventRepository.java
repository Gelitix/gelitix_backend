package com.gelitix.backend.event.entity.repository;

import com.gelitix.backend.event.entity.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event,Long > {
}
