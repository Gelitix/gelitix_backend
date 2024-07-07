package com.gelitix.backend.users.repository;


import com.gelitix.backend.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);
    boolean existsByUsername(String username);
    Optional<Users> findById(Integer id);
    Optional<Users> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<Users> findUserByReferralCode(String referralCode);
    Optional<Users> findUserByEmail(String email);

}
