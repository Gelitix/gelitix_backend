package com.gelitix.backend.auth.service.impl;

import com.gelitix.backend.auth.service.AuthService;
import com.gelitix.backend.users.entity.Users;
import com.gelitix.backend.users.repository.UserRepository;
import lombok.extern.java.Log;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.stream.Collectors;

@Log
@Service
public class AuthServiceImpl implements AuthService  {

    private final JwtEncoder jwtEncoder;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthServiceImpl(JwtEncoder jwtEncoder, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.jwtEncoder = jwtEncoder;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;

    }

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        Optional<Users> user = userRepository.findByEmail(authentication.getName());

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("user not found bruh");
        }

        String roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .claim("roles",roles)
                .claim("userId", user.get().getId())
                .build();

        log.info("Token Requested for user : " + authentication.getName() + " with roles : " + roles);

        var jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return jwt;
    }


}