package com.gelitix.backend.users.dto;

import lombok.Data;

@Data
public class UpdateProfileResponseDto {

    private String name;

    private String phoneNumber;

    private String profilePicture;
}
