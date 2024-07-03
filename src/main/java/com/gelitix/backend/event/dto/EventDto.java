package com.gelitix.backend.event.dto;

import com.gelitix.backend.ticketType.dto.TicketTypeDto;
import lombok.Data;
import java.time.Instant;
import java.util.List;

@Data
public class EventDto {
    private String name;
    private Instant date;
    private Instant startTime;
    private Instant endTime;
    private String location;
    private String description;
    private Boolean isFree;

    private List<TicketTypeDto> ticketTypes;

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Instant getStartTime() {
        return startTime;
    }

    public void setStartTime(Instant startTime) {
        this.startTime = startTime;
    }

    public Instant getEndTime() {
        return endTime;
    }

    public void setEndTime(Instant endTime) {
        this.endTime = endTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getIsFree() {
        return isFree;
    }

    public void setIsFree(Boolean isFree) {
        this.isFree = isFree;
    }


    public List<TicketTypeDto> getTicketTypes() {
        return ticketTypes;
    }

    public void setTicketTypes(List<TicketTypeDto> ticketTypes) {
        this.ticketTypes = ticketTypes;
    }
}
