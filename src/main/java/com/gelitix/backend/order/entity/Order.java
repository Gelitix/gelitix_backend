package com.gelitix.backend.order.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gelitix.backend.event.entity.Event;

import com.gelitix.backend.promoDetail.entity.PromoDetail;
import com.gelitix.backend.review.entity.Review;
import com.gelitix.backend.ticketType.entity.TicketType;
import com.gelitix.backend.users.entity.Users;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "orders_id_gen")
    @SequenceGenerator(name = "orders_id_gen", sequenceName = "orders_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "ticket_quantity")
    private Integer ticketQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_type")
    private TicketType ticketType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promo_id")
    private PromoDetail promo;

    @Column(name = "final_price")
    private BigDecimal finalPrice;

    @OneToMany(mappedBy = "order")
    private Set<Review> reviews = new LinkedHashSet<>();

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "full_name", length = Integer.MAX_VALUE)
    private String fullName;

    @Size(max = 20)
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;

    @Size(max = 50)
    @Column(name = "email", length = 50)
    private String email;

    @Column(name = "identity_card", length = Integer.MAX_VALUE)
    private String identityCard;

}