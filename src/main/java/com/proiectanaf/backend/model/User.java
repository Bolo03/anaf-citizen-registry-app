package com.proiectanaf.backend.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Clasa pentru modelul utilizatorului
 * Metodele getUsername, getPassword, getAuthorities, isAccountNonExpired, isAccountNonLocked, isCredentialsNonExpired, isEnabled sunt metodele necesare pentru implementarea interfetei UserDetails
 * @author Bolohan Marian-Cristian, 333AB
 * @version 7 Decembrie 2024
 */

public class User implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private String email;
    private String role;  // e.g., 'user', 'admin'

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(() -> "ROLE_" + role.toUpperCase());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;  // Account doesn't expire
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;  // Account isn't locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;  // Credentials don't expire
    }

    @Override
    public boolean isEnabled() {
        return true;  // User is enabled
    }

}
