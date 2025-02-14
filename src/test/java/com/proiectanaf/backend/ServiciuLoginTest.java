package com.proiectanaf.backend;

import com.proiectanaf.backend.service.ServiciuLogin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServiciuLoginTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private ServiciuLogin serviciuLogin;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAuthenticateUser_Success() {
        String username = "testUser";
        String password = "password123";
        String hashedPassword = "$2a$10$e.bwViUFj3EJ/aqa4/9YOeOJ2uZrBPv4el3kVmsC44G1U1BaUZzXm"; // Example BCrypt hash

        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(String.class))).thenReturn(hashedPassword);
        when(passwordEncoder.matches(password, hashedPassword)).thenReturn(true);

        boolean result = serviciuLogin.authenticateUser(username, password);
        assertTrue(result);

        verify(jdbcTemplate).queryForObject(anyString(), any(Object[].class), eq(String.class));
        verify(passwordEncoder).matches(password, hashedPassword);
    }

    @Test
    void testAuthenticateUser_WrongPassword() {
        String username = "testUser";
        String password = "wrongPassword";
        String hashedPassword = "$2a$10$e.bwViUFj3EJ/aqa4/9YOeOJ2uZrBPv4el3kVmsC44G1U1BaUZzXm"; // Example BCrypt hash

        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(String.class))).thenReturn(hashedPassword);
        when(passwordEncoder.matches(password, hashedPassword)).thenReturn(false);

        boolean result = serviciuLogin.authenticateUser(username, password);
        assertFalse(result);
        verify(jdbcTemplate).queryForObject(anyString(), any(Object[].class), eq(String.class));
        verify(passwordEncoder).matches(password, hashedPassword);
    }

    @Test
    void testAuthenticateUser_UserNotFound() {
        String username = "nonexistentUser";
        String password = "password123";

        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(String.class)))
                .thenThrow(new EmptyResultDataAccessException(1));

        boolean result = serviciuLogin.authenticateUser(username, password);
        assertFalse(result);

        verify(jdbcTemplate).queryForObject(anyString(), any(Object[].class), eq(String.class));
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void testAuthenticateUser_NullPassword() {
        String username = "testUser";

        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(String.class))).thenReturn(null);

        boolean result = serviciuLogin.authenticateUser(username, "password123");
        assertFalse(result);

        verify(jdbcTemplate).queryForObject(anyString(), any(Object[].class), eq(String.class));
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }
}
