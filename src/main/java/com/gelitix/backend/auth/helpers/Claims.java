package com.gelitix.backend.auth.helpers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Map;

public class Claims {

    public static Map<String, Object> getClaimsFromJwt() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt)) {
            throw new IllegalStateException("JWT not found in SecurityContext");
        }

        Jwt jwt = (Jwt) authentication.getPrincipal();
        return jwt.getClaims();
    }

    public static String getEmailFromJwt() {
        return (String) getClaimsFromJwt().get("sub"); // Assuming 'sub' is the username
    }

    public static String getRoleFromJwt() {
        Object roles = getClaimsFromJwt().get("roles");
        if (roles instanceof String) {
            return (String) roles;
        } else if (roles instanceof Collection) {
            return ((Collection<?>) roles).stream()
                    .map(Object::toString)
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }
}
