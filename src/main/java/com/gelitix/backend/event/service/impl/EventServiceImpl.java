package com.gelitix.backend.event.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.gelitix.backend.event.dto.EventDto;
import com.gelitix.backend.event.entity.Event;
import com.gelitix.backend.event.repository.EventRepository;
import com.gelitix.backend.event.service.EventService;
import com.gelitix.backend.eventCategory.entity.EventCategory;
import com.gelitix.backend.eventCategory.service.EventCategoryService;
import com.gelitix.backend.eventLocation.entity.EventLocation;
import com.gelitix.backend.eventLocation.service.EventLocationService;
import com.gelitix.backend.ticketType.dto.CreateTicketTypeDto;
import com.gelitix.backend.ticketType.entity.TicketType;
import com.gelitix.backend.ticketType.repository.TicketTypeRepository;
import com.gelitix.backend.ticketType.service.TicketTypeService;
import com.gelitix.backend.users.entity.Users;
import com.gelitix.backend.users.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final EventLocationService eventLocationService;
    private final EventCategoryService eventCategoryService;
    private final UserService userService;
    private final TicketTypeService ticketTypeService;
    private final Cloudinary cloudinary;

    public EventServiceImpl(EventRepository eventRepository, TicketTypeRepository ticketTypeRepository, EventLocationService eventLocationService, EventCategoryService eventCategoryService, UserService userService, @Lazy TicketTypeService ticketTypeService, Cloudinary cloudinary) {
        this.eventRepository = eventRepository;
        this.eventLocationService = eventLocationService;
        this.eventCategoryService = eventCategoryService;
        this.userService = userService;
        this.ticketTypeService = ticketTypeService;
        this.cloudinary = cloudinary;
    }

    @Override
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    private void mapDtoToEntity(EventDto eventDto, Event event) {
        event.setId(eventDto.getId());
        event.setName(eventDto.getName());
        event.setDate(eventDto.getDate());
//        event.setStart(LocalTime.ofInstant(eventDto.getStartTime(), ZoneId.systemDefault()));
//        event.setEnd(LocalTime.ofInstant(eventDto.getEndTime(), ZoneId.systemDefault()));
        if (eventDto.getStartTime() != null) {
            event.setStart(LocalTime.ofInstant(eventDto.getStartTime(), ZoneId.systemDefault()));
        }

        if (eventDto.getEndTime() != null) {
            event.setEnd(LocalTime.ofInstant(eventDto.getEndTime(), ZoneId.systemDefault()));
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
        event.setUpdatedAt(Instant.now());
        event = eventRepository.save(event);

        handleImageUpload(eventDto, event);

        if (eventDto.getTicketTypes() != null && !eventDto.getTicketTypes().isEmpty()) {
            for (CreateTicketTypeDto ticketTypeDto : eventDto.getTicketTypes()) {

                ticketTypeService.createTicketType(ticketTypeDto, event.getId());
            }
        }

        return mapEntityToDto(event);
    }

    @Override
    public List<EventDto> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(this::mapEntityToDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventDto getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        return mapEntityToDto(event);
    }

    @Override
    @Transactional
    public EventDto updateEvent(Long id, EventDto eventDto) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found with id: " + id));
        // Preserve the existing user ID
        Long existingUserId = existingEvent.getUser().getId();

        // Update the event with new data
        mapDtoToEntity(eventDto, existingEvent);

        // Ensure the user ID remains unchanged
        Users existingUser = userService.findById(existingUserId);
        existingEvent.setUser(existingUser);

        existingEvent.setUpdatedAt(Instant.now());
        existingEvent = eventRepository.save(existingEvent);

        handleImageUpload(eventDto, existingEvent);

        return mapEntityToDto(existingEvent);
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
        eventDto.setStartTime(event.getStart().atDate(LocalDate.now()).toInstant(ZoneOffset.UTC));
        eventDto.setEndTime(event.getEnd().atDate(LocalDate.now()).toInstant(ZoneOffset.UTC));
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

    private CreateTicketTypeDto mapTicketTypeToDto(TicketType ticketType) {
        CreateTicketTypeDto dto = new CreateTicketTypeDto();
        dto.setName(ticketType.getName());
        dto.setPrice(ticketType.getPrice());
        dto.setQuantity(ticketType.getQuantity());
        return dto;
    }

    private void handleImageUpload(EventDto eventDto, Event event) {
        MultipartFile imageFile = eventDto.getImageFile();
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                Map<String, Object> uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
                String imageUrl = uploadResult.get("url").toString();
                event.setPic(imageUrl);
                eventDto.setImageUrl(imageUrl);
                eventRepository.save(event);
            } catch (IOException e) {
                throw new RuntimeException("Photo upload failed", e);
            }
        }
    }
}


