package com.gelitix.backend.event.dto;

import com.gelitix.backend.ticketType.dto.CreateTicketTypeDto;
import jakarta.annotation.Nullable;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Set;

@Data
public class UpdateEventResponseDto {

        @Nullable
        private String name;

        @Nullable
        private Instant date;

        @Nullable
        private Instant startTime;

        @Nullable
        private Instant endTime;

        @Nullable
        private String location;

        @Nullable
        private String description;

        @Nullable
        private String organizer;

        @Nullable
        private String eventCategory;

        @Nullable
        private Boolean isFree;

        @Nullable
        private Set<CreateTicketTypeDto> ticketTypes;

        @Nullable
        private Long userId;

        @Nullable
        private MultipartFile imageFile;

        @Nullable
        private String imageUrl;

        private Instant updateTime;

    }


