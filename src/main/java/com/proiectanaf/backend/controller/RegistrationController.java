package com.proiectanaf.backend.controller;

import com.proiectanaf.backend.model.User;
import com.proiectanaf.backend.service.ServiciuUser;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Clasa pentru controllerul paginii de inregistrare
 * Aceasta clasa contine metodele pentru afisarea paginii de inregistrare si pentru inregistrarea unui nou utilizator
 * Constructorul initializeaza serviciul de utilizatori si encoderul de parole
 * Metoda registerUser verifica daca datele introduse sunt valide si inregistreaza un nou utilizator
 * @author Bolohan Marian-Cristian, 333AB
 * @version 7 Decembrie 2024
 */

@Controller
public class RegistrationController {

    private final ServiciuUser serviciuUser;
    private final BCryptPasswordEncoder passwordEncoder;

    public RegistrationController(ServiciuUser serviciuUser, BCryptPasswordEncoder passwordEncoder) {
        this.serviciuUser = serviciuUser;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "register";
    }

    // Metoda pentru inregistrarea unui nou utilizator
    @PostMapping("/register")
    public String registerUser(@RequestParam(value = "username", required = false) String username,
                               @RequestParam(value = "password", required = false) String password,
                               @RequestParam(value = "email", required = false) String email,
                               Model model) {
        // Cream un obiect de tip User
        User newUser = new User();

        // VALIDARI USERNAME
        // Verificam daca datele introduse sunt nule sau goale
        if(username == null || password == null || email == null ||
            username.isEmpty() || password.isEmpty() || email.isEmpty()){
            model.addAttribute("errorMessage", "Toate câmpurile sunt obligatorii. ");
            return "register"; // Redirect back to the registration page
        }

        // Verificam daca username-ul are lungimea corecta, intre 5 si 20 de caractere
        if(username.length() < 5){
            model.addAttribute("errorMessage", "Username-ul este prea scurt. ");
            return "register";
        }
        else if(username.length() > 20){
            model.addAttribute("errorMessage", "Username-ul este prea lung. ");
            return "register"; // Redirect back to the registration page
        }

        // Verificam daca username-ul incepe cu o cifra, daca da, afisam eroare
        if(Character.isDigit(username.charAt(0))){
            model.addAttribute("errorMessage", "Username-ul nu poate incepe cu o cifră. ");
            return "register"; // Redirect back to the registration page
        }

        // Verificam daca username-ul contine doar litere si cifre
        for(int i = 0; i < username.length(); i++){
            if(!Character.isLetterOrDigit(username.charAt(i))){
                model.addAttribute("errorMessage", "Username-ul trebuie să conțină doar cifre și litere. ");
                return "register"; // Redirect back to the registration page
            }
        }

        // Verificam daca username-ul este unic in baza de date
        if (serviciuUser.findByUsername(username)) {
            model.addAttribute("errorMessage", "Username-ul este deja folosit. ");
            return "register"; // Redirect back to the registration page
        }

        // VALIDARI PAROLA
        //Verificam daca parola este mai mare de 8 caractere, contine o litera mare, o litera mica, o cifra si un caracter special
        if(password.length() < 8){
            model.addAttribute("errorMessage", "Parola este prea scurtă. ");
            return "register"; // Redirect back to the registration page
        }
        else if(password.equals(password.toLowerCase())){
            model.addAttribute("errorMessage", "Parola trebuie să conțină o literă mare. ");
            return "register"; // Redirect back to the registration page
        }
        else if(password.equals(password.toUpperCase())){
            model.addAttribute("errorMessage", "Parola trebuie să conțină o literă mică. ");
            return "register"; // Redirect back to the registration page
        }
        else if(!password.matches(".*\\d.*")){
            model.addAttribute("errorMessage", "Parola trebuie să conțină cel puțin o cifră. ");
            return "register"; // Redirect back to the registration page
        }
        else if(!password.matches(".*[!@#$%^&*].*")){
            model.addAttribute("errorMessage", "Password Parola trebuie să conțină un caracter special. ");
            return "register"; // Redirect back to the registration page
        }

        // Verificam daca parola contine username-ul
        if(password.contains(username)){
            model.addAttribute("errorMessage", "Parola nu trebuie să conțină username-ul. ");
            return "register"; // Redirect back to the registration page
        }

        // VALIDARI EMAIL
        // Verificam daca email-ul este valid
        if(!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
            model.addAttribute("errorMessage", "Email invalid. ");
            return "register"; // Redirect back to the registration page
        }

        // Verificam daca email-ul este unic in baza de date
        if(serviciuUser.findByEmail(email)){
            model.addAttribute("errorMessage", "Email-ul este deja folosit. ");
            return "register"; // Redirect back to the registration page
        }

        // Daca toate datele sunt valide, le adaugam in obiectul User
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));  // Criptam parola inainte de a o salva in baza de date
        newUser.setEmail(email);
        newUser.setRole("user");

        // Salvam noul utilizator in baza de date
        serviciuUser.save(newUser);

        return "/";  // Redirectionam utilizatorul catre pagina principala dupa inregistrare
    }
}
