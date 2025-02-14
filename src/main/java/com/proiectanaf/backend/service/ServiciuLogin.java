package com.proiectanaf.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * Clasa pentru serviciul de autentificare a utilizatorului in aplicatie
 * Metoda authenticateUser verifica daca parola introdusa de utilizator corespunde cu cea din baza de date
 * @author Bolohan Marian-Cristian, 333AB
 * @version 7 Decembrie 2024
 */


@Service
public class ServiciuLogin {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public boolean authenticateUser(String username, String password) {
        String sql = "SELECT parola FROM \"USERS\" WHERE username = ?";
        // Verificam daca parola introdusa de utilizator corespunde cu cea din baza de date
        // Acest lucru se realizeaza prin compararea hash-urilor parolelor
        try {
            String storedHashedPassword = jdbcTemplate.queryForObject(sql, new Object[]{username}, String.class);
            if (storedHashedPassword != null) {
                return passwordEncoder.matches(password, storedHashedPassword);
            }
        } catch (EmptyResultDataAccessException e) {
            return false;
        }
        return false;
    }

}
