package com.gelitix.backend.users.service;

import com.gelitix.backend.users.dto.ProfileDto;
import com.gelitix.backend.users.dto.RegisterRequestDto;
import com.gelitix.backend.users.dto.UpdateProfileResponseDto;
import com.gelitix.backend.users.entity.Users;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface UserService {

    Users register(RegisterRequestDto user);

    ProfileDto findProfileByEmail(String email);

    Users findById(Long id);

    List<Users> findAll();

    Optional<Users> getUserByEmail(String email);

    UpdateProfileResponseDto updateProfile(String email, ProfileDto profileDto);

    void deleteUser(Long id);

    Users deductPointBalance (Long userId, BigDecimal usedPoint);

}
