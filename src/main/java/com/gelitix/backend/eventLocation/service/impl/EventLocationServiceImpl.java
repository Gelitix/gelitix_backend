package com.gelitix.backend.eventLocation.service.impl;

import com.gelitix.backend.eventLocation.dto.EventLocationDto;
import com.gelitix.backend.eventLocation.entity.EventLocation;
import com.gelitix.backend.eventLocation.repository.EventLocationRepository;
import com.gelitix.backend.eventLocation.service.EventLocationService;
import com.gelitix.backend.users.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
    public class EventLocationServiceImpl implements EventLocationService {
    private final EventLocationRepository eventLocationRepository;
    private final UserRepository userRepository;

    public EventLocationServiceImpl(final EventLocationRepository eventLocationRepository, UserRepository userRepository) {
        this.eventLocationRepository = eventLocationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<EventLocationDto> getEventLocations() {
            return eventLocationRepository.findAll().stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());

    }


    @Override
    public EventLocation findByName(String name) {
        return eventLocationRepository.findByName(name);
    }

    private EventLocationDto mapToDto(EventLocation eventLocation) {
        EventLocationDto dto = new EventLocationDto();
        dto.setId(eventLocation.getId());
        dto.setName(eventLocation.getName());
        return dto;

    }

  }


