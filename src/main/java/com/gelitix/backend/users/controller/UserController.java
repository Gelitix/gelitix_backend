package com.gelitix.backend.users.controller;

import com.gelitix.backend.auth.helpers.Claims;
import com.gelitix.backend.response.Response;
import com.gelitix.backend.users.dto.ProfileDto;
import com.gelitix.backend.users.dto.RegisterRequestDto;
import com.gelitix.backend.users.entity.Users;
import com.gelitix.backend.users.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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

    @PutMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileDto profileDto) {
        var claims = Claims.getClaimsFromJwt();
        var email = (String) claims.get("sub");

        ProfileDto updatedProfile = userService.updateProfile(email, profileDto);
        return Response.success("User updated successfully", updatedProfile);
    }

    @DeleteMapping("/delete-profile")
    public ResponseEntity<?> deleteProfile(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
    }



}
