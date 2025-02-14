package com.proiectanaf.backend;

import com.proiectanaf.backend.model.User;
import com.proiectanaf.backend.repository.UserRowMapper;
import com.proiectanaf.backend.service.ServiciuUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServiciuUserTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private ServiciuUser serviciuUser;

    private User testUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("hashedPassword123");
        testUser.setEmail("test@example.com");
    }

    @Test
    void testFindByUsername_Exists() {
        String username = "testUser";
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(Integer.class))).thenReturn(1);

        boolean result = serviciuUser.findByUsername(username);

        assertTrue(result);

        verify(jdbcTemplate).queryForObject(anyString(), any(Object[].class), eq(Integer.class));
    }

    @Test
    void testFindByUsername_NotExists() {
        String username = "nonexistentUser";
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(Integer.class))).thenReturn(0);

        boolean result = serviciuUser.findByUsername(username);

        assertFalse(result);

        verify(jdbcTemplate).queryForObject(anyString(), any(Object[].class), eq(Integer.class));
    }

    @Test
    void testFindByEmail_Exists() {
        String email = "test@example.com";
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(Integer.class))).thenReturn(1);

        boolean result = serviciuUser.findByEmail(email);

        assertTrue(result);

        verify(jdbcTemplate).queryForObject(anyString(), any(Object[].class), eq(Integer.class));
    }

    @Test
    void testFindByEmail_NotExists() {
        String email = "nonexistent@example.com";
        when(jdbcTemplate.queryForObject(anyString(), any(Object[].class), eq(Integer.class))).thenReturn(0);

        boolean result = serviciuUser.findByEmail(email);

        assertFalse(result);

        verify(jdbcTemplate).queryForObject(anyString(), any(Object[].class), eq(Integer.class));
    }
}
