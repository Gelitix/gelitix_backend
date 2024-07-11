package com.gelitix.backend.users.controller;

import com.gelitix.backend.auth.helpers.Claims;
import com.gelitix.backend.response.Response;
import com.gelitix.backend.users.dto.ProfileDto;
import com.gelitix.backend.users.dto.RegisterRequestDto;
import com.gelitix.backend.users.dto.UpdateProfileResponseDto;
import com.gelitix.backend.users.entity.Users;
import com.gelitix.backend.users.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/user")
@Validated
@Slf4j
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@ModelAttribute @Validated RegisterRequestDto registerRequestDto,
                                      @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        registerRequestDto.setProfileImage(profileImage);
        Users user = userService.register(registerRequestDto);
        if (user == null) {
            return Response.failed("User registration failed");
        }
        log.info("User registered successfully: {}", user.getUsername());
        return Response.success("User registered successfully", user);
    }

    @RolesAllowed({"ROLE_EVENT_ORGANIZER", "ROLE_USER"})
    @GetMapping("/profile")
    public ResponseEntity<?> profile(ProfileDto profileDto) {
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");
        ProfileDto user = userService.findProfileByEmail(email);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return Response.success("User profile", user);
    }

    @GetMapping("/findUserByEmail")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
        Optional<Users> user = userService.getUserByEmail(email);
        return user.map(u ->Response.success(200, "User Found", u)).orElseGet(() -> Response.failed("User Not Found"));
    }

    @RolesAllowed({"ROLE_EVENT_ORGANIZER", "ROLE_USER"})
    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileDto profileDto) {
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");

        UpdateProfileResponseDto updatedProfile = userService.updateProfile(email, profileDto);
        return Response.success("User updated successfully", updatedProfile);
    }

    @RolesAllowed("ROLE_USER")
    @DeleteMapping("/delete-profile/{id}")
    public ResponseEntity<?> deleteProfile(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }
}
