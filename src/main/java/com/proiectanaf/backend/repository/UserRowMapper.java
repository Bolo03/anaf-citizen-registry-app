package com.proiectanaf.backend.repository;

import com.proiectanaf.backend.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clasa pentru maparea unui utilizator din baza de date in obiectul User
 * Metoda mapRow realizeaza maparea unui rand din baza de date in obiectul User
 * @author Bolohan Marian-Cristian, 333AB
 * @version 7 Decembrie 2024
 */

public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("parola"));
        user.setEmail(rs.getString("email"));

        String role = rs.getString("rol");
        user.setRole(role != null ? role : "user");

        return user;
    }
}
