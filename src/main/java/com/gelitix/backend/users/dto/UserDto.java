package com.gelitix.backend.users.dto;

import lombok.Data;

import java.time.Instant;

@Data
public class UserDto {

    private Integer id;
    private String email;
    private String username;

    private String phoneNumber;
    private Instant createdAt;
    private Instant updatedAt;
}
