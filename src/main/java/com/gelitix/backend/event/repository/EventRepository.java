package com.gelitix.backend.event.repository;

import com.gelitix.backend.event.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
Page<Event> findByEventCategory(String eventCategory, Pageable pageable);
}
