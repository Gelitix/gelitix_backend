package com.gelitix.backend.users.dto;

import com.gelitix.backend.users.entity.RoleName;

import jakarta.annotation.Nullable;

import lombok.Data;

@Data
public class ProfileDto {
        @Nullable
        private String username;

        @Nullable
        private String email;

        @Nullable
        private String password;

        @Nullable
        private RoleName role;

        @Nullable
        private String phoneNumber;

        @Nullable
        private String profilePicture;
}
