package com.gelitix.backend.eventCategory.entity;

import com.gelitix.backend.event.entity.Event;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "event_category")
public class EventCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_category_id_gen")
    @SequenceGenerator(name = "event_category_id_gen", sequenceName = "event_category_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 30)
    @Column(name = "name", length = 30)
    private String name;

    @OneToMany(mappedBy = "eventCategory")
    private Set<Event> events = new LinkedHashSet<>();

}