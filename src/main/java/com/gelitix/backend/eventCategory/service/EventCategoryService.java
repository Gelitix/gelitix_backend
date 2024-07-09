package com.gelitix.backend.eventCategory.service;


import com.gelitix.backend.eventCategory.dto.EventCategoryDto;
import com.gelitix.backend.eventCategory.entity.EventCategory;
import com.gelitix.backend.eventCategory.repository.EventCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventCategoryService {

    private final EventCategoryRepository eventCategoryRepository;

    public EventCategoryService(EventCategoryRepository eventCategoryRepository) {
        this.eventCategoryRepository = eventCategoryRepository;
    }

    public List<EventCategoryDto> getAllEventCategories() {
        return eventCategoryRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private EventCategoryDto mapToDto(EventCategory eventCategory) {
        EventCategoryDto dto = new EventCategoryDto();
        dto.setId(eventCategory.getId());
        dto.setName(eventCategory.getName());
        return dto;
    }

    public EventCategory findByName(String name) {
        return eventCategoryRepository.findByName(name);
    }
}