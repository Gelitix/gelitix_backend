package com.gelitix.backend.event.dto;

import com.gelitix.backend.ticketType.dto.TicketTypeDto;
import com.gelitix.backend.ticketType.entity.TicketType;
import lombok.Data;

import java.time.Instant;
import java.time.LocalTime;
import java.util.List;

@Data
public class GetEventByIdResponseDto {
    private Long id;

    private String name;

    private Instant date;

    private LocalTime startTime;

    private LocalTime endTime;

    private String location;

    private String description;

    private String organizer;

    private String eventCategory;

    private Boolean isFree;

    private List<TicketType> ticketTypes;

    private Long userId;
}
