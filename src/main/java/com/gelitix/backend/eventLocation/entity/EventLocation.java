package com.gelitix.backend.eventLocation.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "event_location")
public class EventLocation {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_location_id_gen")
    @SequenceGenerator(name = "event_location_id_gen", sequenceName = "event_location_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;
}