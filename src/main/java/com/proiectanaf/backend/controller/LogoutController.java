package com.proiectanaf.backend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Clasa pentru controllerul de logout
 * Metoda logout face logout si redirectioneaza catre pagina de login
 * @autor Bolohan Marian-Cristian, 333AB
 * @version 7 Decembrie 2024
 */

public class LogoutController
{
    // Handle logout
    @PostMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        return "redirect:/login?logout";
    }
}
