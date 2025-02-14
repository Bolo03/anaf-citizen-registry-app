package com.proiectanaf.backend;

import com.proiectanaf.backend.model.User;
import com.proiectanaf.backend.repository.UserRowMapper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import java.sql.ResultSet;
import java.sql.SQLException;

class UserRowMapperTest {

    @Test
    void testMapRow_validData() throws SQLException {
        ResultSet rs = Mockito.mock(ResultSet.class);

        String username = "testUser";
        String password = "testPassword";
        String email = "testUser@example.com";
        String role = "admin";

        Mockito.when(rs.getString("username")).thenReturn(username);
        Mockito.when(rs.getString("parola")).thenReturn(password);
        Mockito.when(rs.getString("email")).thenReturn(email);
        Mockito.when(rs.getString("rol")).thenReturn(role);

        UserRowMapper rowMapper = new UserRowMapper();

        User user = rowMapper.mapRow(rs, 1);

        assertNotNull(user);
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertEquals(email, user.getEmail());
        assertEquals(role, user.getRole());
    }
}

