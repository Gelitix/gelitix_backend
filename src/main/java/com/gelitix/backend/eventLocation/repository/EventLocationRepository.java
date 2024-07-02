package com.gelitix.backend.eventLocation.repository;

import com.gelitix.backend.eventLocation.entity.EventLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventLocationRepository extends JpaRepository<EventLocation, Long> {
    EventLocation findByName(String name);
}
