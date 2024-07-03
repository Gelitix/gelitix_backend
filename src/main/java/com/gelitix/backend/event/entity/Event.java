package com.gelitix.backend.event.entity;

import com.gelitix.backend.eventCategory.entity.EventCategory;
import com.gelitix.backend.eventLocation.entity.EventLocation;
import com.gelitix.backend.ticketType.entity.TicketType;
import com.gelitix.backend.users.entity.Users;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "event_id_gen")
    @SequenceGenerator(name = "event_id_gen", sequenceName = "event_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 100)
    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "date")
    private Instant date;

    @Column(name = "start")
    private LocalTime start;

    @Column(name = "\"end\"")
    private LocalTime end;

    @Column(name = "pic", length = Integer.MAX_VALUE)
    private String pic;

    @Column(name = "organizer", length = Integer.MAX_VALUE)
    private String organizer;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "is_free")
    private Boolean isFree;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private EventLocation location;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_category_id")
    private EventCategory eventCategory;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private Set<TicketType> ticketTypes;
}
