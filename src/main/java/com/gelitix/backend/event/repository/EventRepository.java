package com.gelitix.backend.event.repository;

import com.gelitix.backend.event.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE e.name LIKE %:name% AND e.eventCategory.name = :eventCategory")
    Page<Event> findByEventCategoryName(@Param("eventCategory") String eventCategory, Pageable pageable, @Param("name") String name);

    List<Event> findByUserId(Long userId);

    @Query("SELECT e FROM Event e WHERE e.name LIKE %:name%")
    Page<Event> findByName(@Param("name") String name, Pageable pageable);
    List<Event> findByNameContainingIgnoreCase(String name);
}
