package com.gelitix.backend.users.service.impl;

import com.gelitix.backend.point.service.PointService;
import com.gelitix.backend.users.dto.ProfileDto;
import com.gelitix.backend.users.dto.RegisterRequestDto;
import com.gelitix.backend.users.entity.Users;
import com.gelitix.backend.users.repository.UserRepository;
import com.gelitix.backend.users.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PointService pointService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, @Lazy PointService pointService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.pointService = pointService;
    }

    @Override
    public Users register(RegisterRequestDto user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already used."); // or throw an exception
        }
        Users newUser = user.toEntity();
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setReferralCode(RandomStringGenerator.generateRandomString(6));
        String referredCode= user.getReferredCode();
        if (referredCode == null || referredCode.isEmpty()) {
            return userRepository.save(newUser);
        }

        var uplineUserOpts = userRepository.findUserByReferralCode(referredCode);
        if (uplineUserOpts.isEmpty()) {
            throw new RuntimeException("Referred code not found.");
        }
        var uplineUser = uplineUserOpts.get();
        int pointsAwarded =10000;
        uplineUser.setPointBalance(uplineUser.getPointBalance() + pointsAwarded);
        userRepository.save(uplineUser);
        var savedUser = userRepository.save(newUser);
        pointService.recordPointHistory(uplineUser,savedUser);

        return savedUser;
    }

    @Override
    public ProfileDto findProfileByEmail(String email) {
        Optional<Users> currentProfile = userRepository.findByEmail(email);
        if (currentProfile.isEmpty()) {
            throw new IllegalArgumentException("Your account cannot be found");
        }
        Users user = currentProfile.get();
        ProfileDto profileDto = new ProfileDto();
        profileDto.setUsername(user.getUsername());
        profileDto.setProfilePicture(user.getProfilePicture());
        profileDto.setEmail(user.getEmail());
        profileDto.setRole(user.getRole());

        return profileDto;
    }

    @Override
    public List<Users> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Users findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public Optional<Users> getUserByEmail(String email)
    {
        return userRepository.findByEmail(email);
    }

    @Override
    public ProfileDto updateProfile(String email, ProfileDto profileDto) {
        Optional<Users> currentProfile = userRepository.findByEmail(email);
        if (currentProfile.isEmpty()) {
            throw new IllegalArgumentException("Your account cannot be found");
        }
        Users user = currentProfile.get();
        profileDto.setUsername(user.getUsername());
        profileDto.setProfilePicture(user.getProfilePicture());
        profileDto.setEmail(user.getEmail());
        profileDto.setRole(user.getRole());

        return profileDto;
    }

    @Override
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Your account cannot be found");
        }
        Users currentUser = userRepository.findById(id).get();
        currentUser.setDeletedAt(Instant.now());
        }
    }


