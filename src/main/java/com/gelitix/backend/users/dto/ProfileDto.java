package com.gelitix.backend.users.dto;

import com.gelitix.backend.users.entity.userRoleEnum.UserRole;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
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
        private UserRole role;

        @Nullable
        private String phoneNumber;

        @Nullable
        private String profilePicture;
}
