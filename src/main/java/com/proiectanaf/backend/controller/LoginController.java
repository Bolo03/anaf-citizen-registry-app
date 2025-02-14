package com.proiectanaf.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.proiectanaf.backend.service.ServiciuLogin;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Clasa pentru controllerul paginii de login
 * Metoda loginPage returneaza pagina login.html
 * Metoda login preia datele din formularul de login si verifica daca datele sunt corecte, daca sunt corecte redirectioneaza catre pagina principala, altfel redirectioneaza catre pagina de login cu un mesaj de eroare
 * @autor Bolohan Marian-Cristian, 333AB
 * @version 7 Decembrie 2024
 */

@Controller
public class LoginController {

    @Autowired
    private ServiciuLogin serviciuLogin;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam(value = "username", required = false) String username,
                        @RequestParam(value = "password",required = false) String password,
                        RedirectAttributes redirectAttributes) {

        // Verificam daca username si parola sunt goale
        if(username == null || password == null || username.isEmpty() || password.isEmpty()){
            redirectAttributes.addFlashAttribute("errorMessage", "Introduceți username și parolă.");
            return "redirect:/login";  // Redirect back to the login page
        }

        // Se inceraca autentificarea utilizatorului
        boolean authenticated = serviciuLogin.authenticateUser(username, password);

        // Daca autentificarea a esuat, redirectionam catre pagina de login cu un mesaj de eroare
        // Daca autentificarea a reusit, redirectionam catre pagina principala
        if(!authenticated){
            redirectAttributes.addFlashAttribute("errorMessage", "Username sau parolă invalidă.");
            return "redirect:/login";  // Redirect back to the login page
        }
        else
        {
            return "redirect:/";  // Redirect to the home page
        }
    }
}
