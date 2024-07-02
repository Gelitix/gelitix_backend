package com.gelitix.backend.users.service.impl;


import com.gelitix.backend.users.dto.ProfileDto;
import com.gelitix.backend.users.dto.RegisterRequestDto;
import com.gelitix.backend.users.entity.Users;
import com.gelitix.backend.users.repository.UserRepository;
import com.gelitix.backend.users.service.UserService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Users register(RegisterRequestDto user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            return null; // or throw an exception
        }
        Users newUser = user.toEntity();
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(newUser);
    }

    @Override
    public ProfileDto findProfileByUsername(String username) {
        Optional<Users> currentProfile = userRepository.findByUsername(username);
        if (currentProfile.isEmpty()) {
            throw new IllegalArgumentException("Your account cannot be found");
        }
        Users user = currentProfile.get();
        ProfileDto profileDto = new ProfileDto();
        profileDto.setUsername(user.getUsername());
        profileDto.setProfilePicture(user.getProfilePicture());
        profileDto.setEmail(user.getEmail());
        profileDto.setRole(user.getRole().name());

        return profileDto;
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
        profileDto.setRole(user.getRole().name());

        return profileDto;
    }

    @Override
    public List<Users> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Users findById(int id) {
        Optional<Users> user = userRepository.findById(id);
        return user.orElse(null); // or throw an exception
    }

    @Override
    public Optional<Users> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public ProfileDto updateProfile(String username, ProfileDto profileDto) {
        Optional<Users> currentProfile = userRepository.findByUsername(username);
        if (currentProfile.isEmpty()) {
            throw new IllegalArgumentException("Your account cannot be found");
        }
        Users user = currentProfile.get();
        profileDto.setUsername(user.getUsername());
        profileDto.setProfilePicture(user.getProfilePicture());
        profileDto.setEmail(user.getEmail());
        profileDto.setRole(user.getRole().name());

        return profileDto;
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Your account cannot be found");
        }
        userRepository.deleteById(id);
        if (userRepository.existsById(id)) {
            throw new IllegalArgumentException("Delete Action Failed");
        }
        }
    }


