package com.gelitix.backend.users.controller;

import com.gelitix.backend.response.Response;
import com.gelitix.backend.users.dto.ProfileDto;
import com.gelitix.backend.users.dto.RegisterRequestDto;
import com.gelitix.backend.users.entity.RoleName;
import com.gelitix.backend.users.entity.Users;
import com.gelitix.backend.users.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.management.relation.Role;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@Validated
@Slf4j
public class UserController {
    private final UserService userService;


    private static final List<String> VALID_ROLES = Arrays.asList(
            RoleName.ROLE_USER.name(),
            RoleName.ROLE_EVENT_ORGANIZER.name()
    );

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Validated RegisterRequestDto registerRequestDto) {
        try {
            if (!VALID_ROLES.contains(RoleName.fromString(registerRequestDto.getRole()).name())) {
                return Response.failed("Invalid role specified");
            }
        } catch (IllegalArgumentException e) {
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
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_EVENT_ORGANIZER')")
    public ResponseEntity<?> profile(ProfileDto profileDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Response.failed("Unauthorized");
        }
        String username = authentication.getName();
        log.info("User profile requested for user: " + username);
        ProfileDto user = userService.findProfileByUsername(username);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return Response.success("User profile", user);
    }

    @GetMapping("/findUserByEmail")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_EVENT_ORGANIZER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
        Optional<Users> user = userService.getUserByEmail(email);
        return user.map(u -> Response.success(200, "User Found", u))
                .orElseGet(() -> Response.failed("User Not Found"));
    }

    @PutMapping("/update-profile")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_EVENT_ORGANIZER')")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileDto profileDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Response.failed("Unauthorized");
        }
        String username = authentication.getName();
        log.info(username);
        ProfileDto updatedProfile = userService.updateProfile(username, profileDto);
        return Response.success("User updated successfully", updatedProfile);
    }

    @DeleteMapping("/delete-profile/{id}")
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_EVENT_ORGANIZER')")
    public ResponseEntity<?> deleteProfile(@PathVariable Long id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Response.failed("Unauthorized");
        }
        userService.deleteUser(id);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }



}
