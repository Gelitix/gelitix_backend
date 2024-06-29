package com.gelitix.backend.users.repository;


import com.gelitix.backend.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {
    Users findByUsername(String username);
    boolean existsByUsername(String username);
    Optional<Users> findById(Integer id);
    Optional<Users> findByEmail(String email);
    boolean existsByEmail(String email);
}
