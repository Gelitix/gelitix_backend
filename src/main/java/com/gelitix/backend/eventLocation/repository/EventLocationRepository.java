package com.gelitix.backend.eventLocation.repository;

import com.gelitix.backend.eventLocation.entity.EventLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventLocationRepository extends JpaRepository<EventLocation, Integer> {
    EventLocation findByName(String name);
}
