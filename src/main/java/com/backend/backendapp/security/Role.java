package com.backend.backendapp.security;

import org.springframework.security.core.GrantedAuthority;

// import jakarta.persistence.Entity;

// @Entity
public enum Role implements GrantedAuthority{
    ROLE_LEARNER,
    ROLE_MENTOR;

    @Override
    public String getAuthority() {
        return name();
    }
}
