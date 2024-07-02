package com.gelitix.backend.users.service;

import com.gelitix.backend.users.dto.ProfileDto;
import com.gelitix.backend.users.dto.RegisterRequestDto;
import com.gelitix.backend.users.entity.Users;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Users register(RegisterRequestDto user);

    ProfileDto findProfileByUsername(String username);

    ProfileDto findProfileByEmail(String username);

    Users findById(int id);

    List<Users> findAll();

    Optional<Users> getUserByEmail(String email);

    ProfileDto updateProfile(String username, ProfileDto profileDto);

    void deleteUser(Long id);
}
