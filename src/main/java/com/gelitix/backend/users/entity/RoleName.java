package com.gelitix.backend.users.entity;

public enum RoleName {
    ROLE_USER,
    ROLE_EVENT_ORGANIZER;

public static RoleName fromString(String role) {
    switch (role.toUpperCase()){
        case "USER":
            return ROLE_USER;
        case "EVENT ORGANIZER":
            return ROLE_EVENT_ORGANIZER;

        default:
            throw new IllegalArgumentException("Unknown role: " + role);
    }
}
}