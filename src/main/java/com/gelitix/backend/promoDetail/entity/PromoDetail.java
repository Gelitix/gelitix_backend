package com.gelitix.backend.promoDetail.entity;

import com.gelitix.backend.event.entity.Event;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLRestriction;

import java.time.Instant;

@Getter
@Setter
@Entity
@SQLRestriction("deleted_at IS NULL")
@Table(name = "promo_details")
public class PromoDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "promo_details_id_gen")
    @SequenceGenerator(name = "promo_details_id_gen", sequenceName = "promo_details_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "is_referral")
    private Boolean isReferral;

    @Column(name = "quantity")
    private Integer quantity;

    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;

    @Column(name = "discount")
    private Double discount;

    @Column(name = "start_valid")
    private Instant startValid;

    @Column(name = "end_valid")
    private Instant endValid;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "deleted_at")
    private Instant deletedAt;

}