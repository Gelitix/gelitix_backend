package com.gelitix.backend.users.controller;

import com.gelitix.backend.response.Response;
import com.gelitix.backend.users.dto.RegisterRequestDto;
import com.gelitix.backend.users.entity.Users;
import com.gelitix.backend.users.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@Validated
@Slf4j
public class UserController {
    private final UserService userService;


    private static final List<String> VALID_ROLES = Arrays.asList("User", "Event Organizer");

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDto registerRequestDto) {

        if (!VALID_ROLES.contains(registerRequestDto.getRole())) {
            return Response.failed("Invalid role specified");
        }
        Users user = userService.register(registerRequestDto);
        if (user == null) {
            return Response.failed("User registration failed");
        }
        log.info("User registered successfully: {}", user.getUsername());
        return Response.success("User registered successfully", user);
    }

    @GetMapping("/profile")
    public ResponseEntity<?> profile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Response.failed("Unauthorized");
        }
        String username = authentication.getName();
        log.info("User profile requested for user: " + username);
        Users user = userService.findByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return Response.success("User profile", user);
    }
}
