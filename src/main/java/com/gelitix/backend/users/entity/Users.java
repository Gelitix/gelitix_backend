package com.gelitix.backend.users.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "users")
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_gen")
    @SequenceGenerator(name = "users_id_gen", sequenceName = "users_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Column(name = "email", length = 50)
    private String email;

    @Size(max = 50)
    @Column(name = "username", length = 50)
    private String username;

    @Column(name = "password", length = Integer.MAX_VALUE)
    private String password;

    @Column(name = "name", length = Integer.MAX_VALUE)
    private String name;

    @Column(name = "profile_picture", length = Integer.MAX_VALUE)
    private String profilePicture;

    @Column(name = "is_referred")
    private Boolean isReferred;

    @ColumnDefault("0")
    @Column(name = "point_balance")
    private Integer pointBalance;

    @Column(name = "role", length = Integer.MAX_VALUE)
    private String role;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "referral_code", length = Integer.MAX_VALUE)
    private String referralCode;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "deleted_at")
    private Instant deletedAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Instant.now();
    }

    @PreRemove
    public void preRemove() {
        this.deletedAt = Instant.now();
    }

}