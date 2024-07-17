package com.gelitix.backend.ticketType.dto;

import com.gelitix.backend.event.entity.Event;
import com.gelitix.backend.order.entity.Order;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;


@Data
    public class GetTicketTypeByIdResponseDto {

        private Long id;

        private String name;

        private BigDecimal price;

        private Integer quantity;

        private String eventName;



    }

