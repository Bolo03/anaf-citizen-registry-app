package com.proiectanaf.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Clasa pentru controllerul paginii de start
 * Metoda home returneaza pagina home.html
 * @author Bolohan Marian-Cristian, 333AB
 * @version 7 Decembrie 2024
 */

@Controller
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "home";
    }
}