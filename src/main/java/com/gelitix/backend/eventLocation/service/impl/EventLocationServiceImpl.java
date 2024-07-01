package com.gelitix.backend.eventLocation.service.impl;

import com.gelitix.backend.eventLocation.entity.EventLocation;
import com.gelitix.backend.eventLocation.repository.EventLocationRepository;
import com.gelitix.backend.eventLocation.service.EventLocationService;
import com.gelitix.backend.users.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
    public class EventLocationServiceImpl implements EventLocationService {
    private final EventLocationRepository eventLocationRepository;
    private final UserRepository userRepository;

    public EventLocationServiceImpl(final EventLocationRepository eventLocationRepository, UserRepository userRepository) {
        this.eventLocationRepository = eventLocationRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<EventLocation> getEventLocations() {
        return eventLocationRepository.findAll();
    }

  }


