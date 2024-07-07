package com.gelitix.backend.eventCategory.controller;

import com.gelitix.backend.eventCategory.dto.EventCategoryDto;
import com.gelitix.backend.eventCategory.entity.EventCategory;
import com.gelitix.backend.eventCategory.service.EventCategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/event-categories")
public class EventCategoryController {

    private final EventCategoryService eventCategoryService;

    public EventCategoryController(EventCategoryService eventCategoryService) {
        this.eventCategoryService = eventCategoryService;
    }

    @GetMapping
    public List<EventCategoryDto> getAllEventCategories() {
        return eventCategoryService.getAllEventCategories();
    }
}