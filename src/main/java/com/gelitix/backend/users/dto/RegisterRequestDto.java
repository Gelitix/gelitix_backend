package com.gelitix.backend.users.dto;

import com.gelitix.backend.users.entity.Users;
import com.gelitix.backend.users.entity.userRoleEnum.UserRole;
import jakarta.annotation.Nullable;
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

    @NotBlank(message = "Role is required")
    private UserRole role;

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
