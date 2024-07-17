package com.gelitix.backend.event.service.impl;

import com.gelitix.backend.cloudinary.service.ImageUploadService;
import com.gelitix.backend.event.dto.EventDto;
import com.gelitix.backend.event.dto.EventNameDto;
import com.gelitix.backend.event.dto.UpdateEventDto;
import com.gelitix.backend.event.dto.UpdateEventResponseDto;
import com.gelitix.backend.event.entity.Event;
import com.gelitix.backend.event.repository.EventRepository;
import com.gelitix.backend.event.service.EventService;
import com.gelitix.backend.eventCategory.entity.EventCategory;
import com.gelitix.backend.eventCategory.service.EventCategoryService;
import com.gelitix.backend.eventLocation.entity.EventLocation;
import com.gelitix.backend.eventLocation.service.EventLocationService;
import com.gelitix.backend.ticketType.dto.CreateTicketTypeDto;
import com.gelitix.backend.ticketType.entity.TicketType;
import com.gelitix.backend.ticketType.service.TicketTypeService;
import com.gelitix.backend.users.entity.Users;
import com.gelitix.backend.users.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;


import org.springframework.data.domain.Pageable;
import java.time.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventLocationService eventLocationService;
    private final EventCategoryService eventCategoryService;
    private final UserService userService;
    private final TicketTypeService ticketTypeService;
    private final ImageUploadService imageUploadService;

    public EventServiceImpl(EventRepository eventRepository, EventLocationService eventLocationService, EventCategoryService eventCategoryService, UserService userService, @Lazy TicketTypeService ticketTypeService, ImageUploadService imageUploadService) {
        this.eventRepository = eventRepository;
        this.eventLocationService = eventLocationService;
        this.eventCategoryService = eventCategoryService;
        this.userService = userService;
        this.ticketTypeService = ticketTypeService;
        this.imageUploadService = imageUploadService;

    }

    @Override
    public void deleteEvent(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new IllegalArgumentException("Event with id " + id + " does not exist");
        }
        Event currentEvent = eventRepository.findById(id).get();
        currentEvent.setDeletedAt(Instant.now());
        eventRepository.save(currentEvent);
    }

    @Override
    @Transactional
    public EventDto createEvent(EventDto eventDto) {
        Event event = new Event();
        mapDtoToEntity(eventDto, event);

        Users user = userService.findById(eventDto.getUserId());
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        event.setUser(user);
        event.setCreatedAt(Instant.now());
        event = getEvent(eventDto, event);

        String imageUrl = imageUploadService.uploadImage(eventDto.getImageFile());
        if (imageUrl != null) {
            event.setPic(imageUrl);
            eventDto.setImageUrl(imageUrl);
            eventRepository.save(event);}

        if (eventDto.getTicketTypes() != null && !eventDto.getTicketTypes().isEmpty()) {
            for (CreateTicketTypeDto ticketTypeDto : eventDto.getTicketTypes()) {

                ticketTypeService.createTicketType(ticketTypeDto, event.getId());
            }
        }

        return mapEntityToDto(event);
    }

    private Event getEvent(EventDto eventDto, Event event) {
        event.setUpdatedAt(Instant.now());
        event = eventRepository.save(event);

        return event;
    }

    @Override
    public Page<Event> getAllEvents(String eventCategory, Pageable pageable, String order, String sort, String search) {
//        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
//        Sort sortObject = Sort.by(direction, sort);
        Pageable pageableWithSort = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        Page<Event> eventPage;
        if (eventCategory != null && !eventCategory.isEmpty()) {
            eventPage = eventRepository.findByEventCategoryName(eventCategory, pageableWithSort, search);
        } else if (search != null && !search.isEmpty()) {
            eventPage = eventRepository.findByName(search, pageableWithSort);
        } else {
            eventPage = eventRepository.findAll(pageableWithSort);
        }
        return eventPage;
    }

    @Override
    public EventDto getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        return mapEntityToDto(event);
    }

    @Override
    @Transactional
    public UpdateEventResponseDto updateEvent(Long id, UpdateEventDto eventDto) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        // Preserve the existing user ID
        Long existingUserId = existingEvent.getUser().getId();

        // Update the event with new data
        if (eventDto.getName() != null && !eventDto.getName().isEmpty()) {
            existingEvent.setName(eventDto.getName());
        }

        if (eventDto.getDate() != null){
            existingEvent.setDate(eventDto.getDate());
        }

        if (eventDto.getDescription() != null && !eventDto.getDescription().isEmpty()) {
            existingEvent.setDescription(eventDto.getDescription());
        }
        if (eventDto.getEventCategory() != null && !eventDto.getEventCategory().isEmpty()) {
            existingEvent.setEventCategory(eventCategoryService.findByName(eventDto.getEventCategory()));
        }
        if (eventDto.getEndTime() != null) {
            existingEvent.setEnd(eventDto.getEndTime());
        }
        if (eventDto.getStartTime() != null) {
            existingEvent.setStart(eventDto.getStartTime());
        }
        if (eventDto.getImageUrl() != null && !eventDto.getImageUrl().isEmpty()) {
            existingEvent.setPic(eventDto.getImageUrl());
        }
        if (eventDto.getIsFree() != null) {
            existingEvent.setIsFree(eventDto.getIsFree());
        }
        if (eventDto.getLocation() != null && !eventDto.getLocation().isEmpty()) {
            existingEvent.setLocation(eventLocationService.findByName(eventDto.getLocation()));
        }
        if (eventDto.getOrganizer() != null && !eventDto.getOrganizer().isEmpty()) {
            existingEvent.setOrganizer(eventDto.getOrganizer());
        }
        if (eventDto.getTicketTypes() != null && !eventDto.getTicketTypes().isEmpty()) {
            Set<TicketType> newTicketTypes = eventDto.getTicketTypes().stream()
                    .map(this::convertToTicketType)
                    .collect(Collectors.toSet());
            existingEvent.setTicketTypes(newTicketTypes);
        }

        // Ensure the user ID remains unchanged
        Users existingUser = userService.findById(existingUserId);
        existingEvent.setUser(existingUser);


        eventRepository.save(existingEvent);

        UpdateEventResponseDto response = new UpdateEventResponseDto();
        response.setName(existingEvent.getName());
        response.setDescription(existingEvent.getDescription());
        response.setEventCategory(String.valueOf(existingEvent.getEventCategory()));
        response.setEndTime(existingEvent.getEnd());
        response.setStartTime(existingEvent.getStart());
        response.setImageUrl(existingEvent.getPic());
        response.setUpdateTime(existingEvent.getUpdatedAt());
        response.setIsFree(existingEvent.getIsFree());
        response.setLocation(String.valueOf(existingEvent.getLocation()));
        response.setOrganizer(existingEvent.getOrganizer());
        response.setUserId(existingUser.getId());
        return response;
    }


    private TicketType convertToTicketType(CreateTicketTypeDto dto) {
        TicketType ticketType = new TicketType();
        if (dto.getName() != null && !dto.getName().isEmpty()) {
            ticketType.setName(dto.getName());
        }
        if (dto.getPrice() != null ){
            ticketType.setPrice(dto.getPrice());
        }
        if(dto.getQuantity() !=null && dto.getQuantity() > 0){
            ticketType.setQuantity(dto.getQuantity());
        }
        return ticketType;
    }

    @Override
    public Event getEventEntityById(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
    }

    // ... other methods remain the same

    // Use your existing mapEntityToDto method
    @Override
    public EventDto mapEntityToDto(Event event) {
        EventDto eventDto = new EventDto();
        eventDto.setId(event.getId());
        eventDto.setName(event.getName());
        eventDto.setDate(event.getDate());
        eventDto.setStartTime(event.getStart());
        eventDto.setEndTime(event.getEnd());
        eventDto.setDescription(event.getDescription());
        eventDto.setOrganizer(event.getOrganizer());
        eventDto.setLocation(event.getLocation().getName());
        eventDto.setEventCategory(event.getEventCategory().getName());
        eventDto.setIsFree(event.getIsFree());
        eventDto.setUserId(event.getUser().getId());
        eventDto.setImageUrl(event.getPic());

        List<TicketType> ticketTypes = ticketTypeService.getTicketTypesByEvent(event);
        if (ticketTypes != null && !ticketTypes.isEmpty()) {
            List<CreateTicketTypeDto> ticketTypeDtos = ticketTypes.stream()
                    .map(this::mapTicketTypeToDto)
                    .collect(Collectors.toList());
            eventDto.setTicketTypes(ticketTypeDtos);
        }
        return eventDto;

    }

    public List<Event> findEventByUserId(Long userId){
        return eventRepository.findByUserId(userId);
    }

    public CreateTicketTypeDto mapTicketTypeToDto(TicketType ticketType) {
        CreateTicketTypeDto dto = new CreateTicketTypeDto();
        dto.setId(ticketType.getId());
        dto.setName(ticketType.getName());
        dto.setPrice(ticketType.getPrice());
        dto.setQuantity(ticketType.getQuantity());
        return dto;
    }

    public void mapDtoToEntity(EventDto eventDto, Event event) {
        event.setId(eventDto.getId());
        event.setName(eventDto.getName());
        event.setDate(eventDto.getDate());
//        event.setStart(LocalTime.ofInstant(eventDto.getStartTime(), ZoneId.systemDefault()));
//        event.setEnd(LocalTime.ofInstant(eventDto.getEndTime(), ZoneId.systemDefault()));
            if (eventDto.getStartTime() != null) {
                event.setStart(eventDto.getStartTime());
            }

            if (eventDto.getEndTime() != null) {
                event.setEnd(eventDto.getEndTime());
        }
        event.setDescription(eventDto.getDescription());
        EventCategory eventCategory = eventCategoryService.findByName(eventDto.getEventCategory());
        event.setEventCategory(eventCategory);
        event.setIsFree(eventDto.getIsFree());
        event.setOrganizer(eventDto.getOrganizer());

        EventLocation location = eventLocationService.findByName(eventDto.getLocation());
        event.setLocation(location);
//        Users user = userService.findById(eventDto.getUserId()); // Fetch the user using UserService
//        event.setUser(user);
    }

    @Override
    public List<EventDto> getEventsByUserEmail(String email) {
        Optional<Users> currentUserOpts = userService.getUserByEmail(email);
        Users currentUser = currentUserOpts.orElseThrow(() -> new RuntimeException("User not found"));
        Long currentUserId = currentUser.getId();
        List<Event> events = eventRepository.findByUserId(currentUserId);
        return events.stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<Event> findByName(String name) {
        return eventRepository.getEventByName(name);
    }

    @Override
    public List<EventNameDto> getAllEventNames() {
        return eventRepository.findAll().stream()
                .map(this::mapToEventNameDto)
                .collect(Collectors.toList());
    }

    private EventNameDto mapToEventNameDto(Event event) {
        EventNameDto dto = new EventNameDto();
        dto.setId(event.getId());
        dto.setName(event.getName());
        return dto;
    }

    @Override
    public List<String> findEventNamesByName(String name) {
        List<Event> searchResult = eventRepository.findByNameContainingIgnoreCase(name);
        return searchResult.stream()
                .map(Event::getName)
                .limit(10)  // Limit the results to avoid overwhelming the frontend
                .collect(Collectors.toList());
    }


}


