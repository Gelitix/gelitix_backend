package com.gelitix.backend.users.service.impl;

import com.gelitix.backend.cloudinary.service.ImageUploadService;
import com.gelitix.backend.event.service.EventService;
import com.gelitix.backend.point.service.PointService;
import com.gelitix.backend.users.dto.ProfileDto;
import com.gelitix.backend.users.dto.RegisterRequestDto;
import com.gelitix.backend.users.dto.UpdateProfileResponseDto;
import com.gelitix.backend.users.entity.Users;
import com.gelitix.backend.users.repository.UserRepository;
import com.gelitix.backend.users.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PointService pointService;
    private final ImageUploadService imageUploadService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder,ImageUploadService imageUploadService, @Lazy PointService pointService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.pointService = pointService;
        this.imageUploadService = imageUploadService;

    }

    @Override
    public Users register(RegisterRequestDto user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("Email already used."); // or throw an exception
        }
        Users newUser = user.toEntity();
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setReferralCode(RandomStringGenerator.generateRandomString(6));
        newUser.setUsername(user.getName());
        newUser.setCreatedAt(Instant.now());
        newUser.setUpdatedAt(Instant.now());
        String referredCode= user.getReferredCode();

        MultipartFile profileImage = user.getProfileImage();
        if (profileImage != null && !profileImage.isEmpty()) {
            String imageUrl = imageUploadService.uploadImage(profileImage);
            newUser.setProfilePicture(imageUrl);
        }

        if (referredCode == null || referredCode.isEmpty()) {
            return userRepository.save(newUser);
        }
        newUser.setIsReferred(true);
        var uplineUserOpts = userRepository.findUserByReferralCode(referredCode);
        if (uplineUserOpts.isEmpty()) {
            throw new RuntimeException("Referred code not found.");
        }
        var uplineUser = uplineUserOpts.get();
        BigDecimal pointsAwarded = BigDecimal.valueOf(10000);
        uplineUser.setPointBalance(uplineUser.getPointBalance().add(pointsAwarded));
        userRepository.save(uplineUser);
        newUser.setIsReferred(true);
        var savedUser = userRepository.save(newUser);
        pointService.recordPointHistory(uplineUser,savedUser);

        return savedUser;
    }

    @Override
    public Users deductPointBalance (Long userId, BigDecimal usedPoint) {
        Users currentUser= userRepository.findById(userId).orElseThrow(()-> new UsernameNotFoundException("User not found")) ;
        currentUser.setPointBalance(currentUser.getPointBalance().subtract(usedPoint)); ;
        return currentUser;
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
        profileDto.setPassword(user.getPassword());
        profileDto.setPhoneNumber(user.getPhoneNumber());
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
    public Users findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public Optional<Users> getUserByEmail(String email)
    {
        return userRepository.findByEmail(email);
    }

    @Override
    public UpdateProfileResponseDto updateProfile(String email, ProfileDto profileDto) {
        Optional<Users> currentProfile = userRepository.findByEmail(email);
        if (currentProfile.isEmpty()) {
            throw new IllegalArgumentException("Your account cannot be found");
        }
        Users user = currentProfile.get();
        user.setUsername(profileDto.getUsername());
        user.setProfilePicture(profileDto.getProfilePicture());
        user.setPhoneNumber(profileDto.getPhoneNumber());
        user.setRole(profileDto.getRole());
        userRepository.save(user);

        UpdateProfileResponseDto updateProfileResponse = new UpdateProfileResponseDto();
        updateProfileResponse.setUsername(user.getUsername());
        updateProfileResponse.setProfilePicture(user.getProfilePicture());
        updateProfileResponse.setPhoneNumber(user.getPhoneNumber());
        updateProfileResponse.setPassword(user.getPassword());

        return updateProfileResponse;
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Your account cannot be found");
        }
        Users currentUser = userRepository.findById(id).get();
        currentUser.setDeletedAt(Instant.now());
        userRepository.save(currentUser);
        }
    }