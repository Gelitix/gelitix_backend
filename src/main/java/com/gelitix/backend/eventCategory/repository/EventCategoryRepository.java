package com.gelitix.backend.eventCategory.repository;

import com.gelitix.backend.eventCategory.entity.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventCategoryRepository extends JpaRepository<EventCategory, Long> {
    EventCategory findByName(String eventCategory);

}