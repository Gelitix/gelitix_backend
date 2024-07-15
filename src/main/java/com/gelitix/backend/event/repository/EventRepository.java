package com.gelitix.backend.event.repository;

import com.gelitix.backend.event.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
Page<Event> findByEventCategoryName(String eventCategory, Pageable pageable);
List<Event> findByUserId(Long userId);
List<Event>  getEventByName(String name);

}
