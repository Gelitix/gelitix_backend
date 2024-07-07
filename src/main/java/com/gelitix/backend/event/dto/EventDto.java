package com.gelitix.backend.event.dto;

import com.gelitix.backend.ticketType.dto.TicketTypeDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class EventDto {

    private Integer id;

    @NotBlank(message = "Event name is mandatory")
    private String name;

    @NotNull(message = "Event date is mandatory")
    private Instant date;

    @NotNull(message = "Event start time is mandatory")
    private Instant startTime;

    @NotNull(message = "Event end time is mandatory")
    private Instant endTime;

    @NotBlank(message = "Event location is mandatory")
    private String location;

    @NotBlank(message = "Event description is mandatory")
    private String description;

    @NotBlank(message = "Event organizer is mandatory")
    private String organizer;

    @NotBlank(message = "Event category is mandatory")
    private String eventCategory;

    @NotNull(message = "Free status is mandatory")
    private Boolean isFree;

    @NotNull(message = "Ticket types are mandatory")
    private List<TicketTypeDto> ticketTypes;

    private Integer userId;

}
