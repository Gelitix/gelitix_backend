package com.gelitix.backend.users.service;

import com.gelitix.backend.users.dto.RegisterRequestDto;
import com.gelitix.backend.users.entity.Users;

import java.util.List;

public interface UserService {

    Users register(RegisterRequestDto user);

    Users findByUsername(String username);

    Users findById(int id);

    List<Users> findAll();

    Users profile();


}
