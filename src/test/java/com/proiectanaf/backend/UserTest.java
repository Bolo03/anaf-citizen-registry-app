package com.proiectanaf.backend;

import com.proiectanaf.backend.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testGettersAndSetters() {
        User user = new User();

        user.setId(1L);
        assertEquals(1L, user.getId());

        user.setUsername("testUser");
        assertEquals("testUser", user.getUsername());

        user.setPassword("password123");
        assertEquals("password123", user.getPassword());

        user.setEmail("testUser@example.com");
        assertEquals("testUser@example.com", user.getEmail());

        user.setRole("admin");
        assertEquals("admin", user.getRole());
    }

    @Test
    void testGetAuthorities() {
        User user = new User();
        user.setRole("user");

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_USER")));
    }

    @Test
    void testAccountStatusMethods() {
        User user = new User();

        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
        assertTrue(user.isEnabled());
    }

    @Test
    void testRoleFormattingInAuthorities() {
        User user = new User();
        user.setRole("admin");

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream().anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
    }
}
