package com.gelitix.backend.users.dto;

import com.gelitix.backend.users.entity.RoleName;
import com.gelitix.backend.users.entity.Users;
import jakarta.annotation.Nullable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequestDto {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;


    @Enumerated(EnumType.STRING)
    private RoleName role;

    @NotBlank (message = "Phone number is required")
    private String phoneNumber;

    @Nullable
    private String referredCode;

    public Users toEntity() {
        Users users = new Users();
        users.setUsername(username);
        users.setEmail(email);
        users.setPassword(password);
        users.setRole(role);
        users.setPhoneNumber(phoneNumber);
        return users;
    }
}
