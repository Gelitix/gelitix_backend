package com.gelitix.backend.users.dto;

import lombok.Data;

@Data
public class UpdateProfileResponseDto {
    private String username;

    private String password;

    private String phoneNumber;

    private String profilePicture;
}
