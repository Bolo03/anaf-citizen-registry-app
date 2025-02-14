package com.proiectanaf.backend.service;

import com.proiectanaf.backend.model.User;
import com.proiectanaf.backend.repository.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Clasa pentru serviciul de inregistrare a utilizatorului in baza de date
 * Metoda loadUserByUsername incarca un utilizator din baza de date
 * Metoda save insereaza un utilizator in baza de date
 * Metoda findByUsername verifica daca un utilizator cu acelasi username exista deja in baza de date
 * Metoda findByEmail verifica daca un utilizator cu acelasi email exista deja in baza de date
 * @author Bolohan Marian-Cristian, 333AB
 * @version 7 Decembrie 2024
 */


@Service
public class ServiciuUser implements UserDetailsService {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ServiciuUser(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Cautam utilizatorul in baza de date
        String sql = "SELECT * FROM \"USERS\" WHERE username = ?";
        User user = jdbcTemplate.queryForObject(sql, new Object[]{username}, new UserRowMapper());
        return user;
    }

    public void save(User user) {
        // Adaogam utilizatorul in baza de date
        String sql = "INSERT INTO \"USERS\" (username, parola, email, rol) VALUES (?, ?, ?, 'user')";
        jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getEmail());
    }

    public boolean findByUsername(String username) {
        // Verificam daca username-ul exista deja in baza de date
        String sql = "SELECT COUNT(*) FROM \"USERS\" WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{username}, Integer.class) > 0;
    }

    public boolean findByEmail(String email) {
        // Verificam daca email-ul exista deja in baza de date
        String sql = "SELECT COUNT(*) FROM \"USERS\" WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{email}, Integer.class) > 0;
    }
}
