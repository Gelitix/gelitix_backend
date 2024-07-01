package com.gelitix.backend.eventCategory.service;


import com.gelitix.backend.eventCategory.entity.EventCategory;
import com.gelitix.backend.eventCategory.repository.EventCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventCategoryService {

    private final EventCategoryRepository eventCategoryRepository;

    public EventCategoryService(EventCategoryRepository eventCategoryRepository) {
        this.eventCategoryRepository = eventCategoryRepository;
    }

    public List<EventCategory> getAllEventCategories() {
        return eventCategoryRepository.findAll();
    }
}