package com.gelitix.backend.users.dto;

import com.gelitix.backend.users.entity.RoleName;
import com.gelitix.backend.users.entity.Users;
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
    private String role;

    @NotBlank (message = "Phone number is required")
    private String phoneNumber;

    public Users toEntity() {
        Users users = new Users();
        users.setUsername(username);
        users.setEmail(email);
        users.setPassword(password);
        users.setRole(RoleName.fromString(role));
        users.setPhoneNumber(phoneNumber);
        return users;
    }
}
