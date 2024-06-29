package com.gelitix.backend.users.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ProfileDto {
        @NotBlank(message = "Username is required")
        private String username;

        @NotBlank(message = "Email is required")
        private String email;

        @NotBlank(message = "Password is required")
        private String password;

        @NotBlank(message = "Role is required")
        private String role;

        @NotBlank (message = "Phone number is required")
        private String phoneNumber;

        @NotBlank
        private String profilePicture;
}
