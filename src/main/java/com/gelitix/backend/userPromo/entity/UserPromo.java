package com.gelitix.backend.userPromo.entity;

import com.gelitix.backend.promoDetail.entity.PromoDetail;
import com.gelitix.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_promo")
public class UserPromo {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_promo_id_gen")
    @SequenceGenerator(name = "user_promo_id_gen", sequenceName = "user_promo_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promo_details")
    private PromoDetail promoDetails;

}