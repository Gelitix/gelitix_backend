package com.gelitix.backend.event.dto;

import lombok.Data;

import java.time.Instant;
import java.time.LocalTime;

@Data
public class EventResponseDto {
    private Long id;
    private String name;
    private Instant date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String pic;
    private String organizer;
    private String description;
    private Boolean isFree;
    private Instant createdAt;
    private Instant updatedAt;
    private String locationName;
    private String eventCategoryName;
}
