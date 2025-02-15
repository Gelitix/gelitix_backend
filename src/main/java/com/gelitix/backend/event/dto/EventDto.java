package com.gelitix.backend.event.dto;

import com.gelitix.backend.ticketType.dto.CreateTicketTypeDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

@Data
public class EventDto {

    private Long id;

    @NotBlank(message = "Event name is mandatory")
    private String name;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant date;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Instant startTime;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
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
    private List<CreateTicketTypeDto> ticketTypes;

    private Long userId;

    private MultipartFile imageFile;

    private String imageUrl;

}
