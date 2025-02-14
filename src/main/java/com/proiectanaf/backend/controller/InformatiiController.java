package com.proiectanaf.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

/**
 * Clasa pentru controlul paginii de informatii personale a utilizatorului
 * Aceasta clasa contine metodele pentru afisarea si actualizarea informatiilor personale ale utilizatorului
 * @author Bolohan Marian-Cristian, 333AB
 * @version 8 Decembrie 2024
 */

@Controller
public class InformatiiController {
    private static boolean userExists = false;
    private static boolean adressExists = false;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/informatii")
    public String home(Model model) {
        // Luam username-ul utilizatorului curent pentru a-l aifsa in pagina
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);

        // Interogare pentru a cauta datele personale ale utilizatorului
        String sql1 = "SELECT \"PERSOANA\".\"nume\", \"PERSOANA\".\"prenume\", \"PERSOANA\".\"CNP\", \"PERSOANA\".\"data_nasterii\", \"PERSOANA\".\"sex\", \"PERSOANA\".\"email\", \"PERSOANA\".\"telefon\" " +
                "FROM \"PERSOANA\" " +
                "INNER JOIN \"USERS\" ON \"PERSOANA\".\"ID_persoana\" = \"USERS\".\"ID_persoana\" " +
                "WHERE \"USERS\".\"username\" = ?";
        Map<String, Object> persoana = null;

        try {
            persoana = jdbcTemplate.queryForMap(sql1, username);
            // Daca utilizatorul exista, Trimitem datele utilizatorului catre view
            userExists = true;
            model.addAttribute("nume_persoana", persoana.get("nume"));
            model.addAttribute("prenume_persoana", persoana.get("prenume"));
            model.addAttribute("cnp_persoana", persoana.get("CNP"));
            model.addAttribute("data_nasterii_persoana", persoana.get("data_nasterii"));
            model.addAttribute("sex_persoana", persoana.get("sex"));
            model.addAttribute("email_persoana", persoana.get("email"));
            model.addAttribute("telefon_persoana", persoana.get("telefon"));
        }
        catch (EmptyResultDataAccessException e) {
            // Daca nu exista date despre utilizator, trimitem valori goale pentru formular
            userExists = false;
            model.addAttribute("nume_persoana", "");
            model.addAttribute("prenume_persoana", "");
            model.addAttribute("cnp_persoana", "");
            model.addAttribute("data_nasterii_persoana", "");
            model.addAttribute("sex_persoana", "");
            model.addAttribute("email_persoana", "");
            model.addAttribute("telefon_persoana", "");
        }

        // Interogare pentru a cauta adresa utilizatorului
        String sql2 = "SELECT \"ADRESA\".\"judet\", \"ADRESA\".\"oras\", \"ADRESA\".\"strada\", \"ADRESA\".\"numarul\", \"ADRESA\".\"cod_postal\" " +
                "FROM \"ADRESA\" " +
                "INNER JOIN \"PERSOANA\" ON \"ADRESA\".\"ID_persoana\" = \"PERSOANA\".\"ID_persoana\" " +
                "INNER JOIN \"USERS\" ON \"PERSOANA\".\"ID_persoana\" = \"USERS\".\"ID_persoana\" " +
                "WHERE \"USERS\".\"username\" = ?";

        Map<String, Object> adresa = null;
        try {
            adresa = jdbcTemplate.queryForMap(sql2, username);
            // Daca adresa exista, Trimitem datele utilizatorului catre view
            adressExists = true;
            model.addAttribute("judet", adresa.get("judet"));
            model.addAttribute("oras", adresa.get("oras"));
            model.addAttribute("strada", adresa.get("strada"));
            model.addAttribute("numar", adresa.get("numarul"));
            model.addAttribute("cod_postal", adresa.get("cod_postal"));

        } catch (EmptyResultDataAccessException e) {
            // Daca nu exista date despre adresa, trimitem valori goale pentru formular
            adressExists = false;
            model.addAttribute("judet", "");
            model.addAttribute("oras", "");
            model.addAttribute("strada", "");
            model.addAttribute("numar", "");
            model.addAttribute("cod_postal", "");
        }
        return "informatii";
    }


    // Metoda pentru actualizarea informatiilor personale ale utilizatorului
    @PostMapping("/informatii")
    public String informatii(@RequestParam(name = "nume_persoana", required = false) String nume_persoana,
                             @RequestParam(name = "prenume_persoana", required = false) String prenume_persoana,
                             @RequestParam(name = "cnp_persoana", required = false) String cnp_persoana,
                             @RequestParam(name = "data_nasterii_persoana", required = false) String data_nasterii_persoana,
                             @RequestParam(name = "sex_persoana", required = false) String sex_persoana,
                             @RequestParam(name = "email_persoana", required = false) String email_persoana,
                             @RequestParam(name = "telefon_persoana", required = false) String telefon_persoana,
                             RedirectAttributes redirectAttributes) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Verificam daca campurile din formular sunt goale
        if(nume_persoana.isEmpty() || prenume_persoana.isEmpty() || cnp_persoana.isEmpty() || data_nasterii_persoana.isEmpty() || sex_persoana.isEmpty() || email_persoana.isEmpty() || telefon_persoana.isEmpty()){
            redirectAttributes.addFlashAttribute("errorMessageInfo", "Toate câmpurile sunt obligatorii. ");
            return "redirect:/informatii";
        }

        // Varificam daca CNP-ul are 13 caractere si contine doar cifre
        if(cnp_persoana.length() != 13){
            redirectAttributes.addFlashAttribute("errorMessageInfo", "CNP-ul trebuie să conțină exact 13 caractere. ");
            return "redirect:/informatii";
        }
        else if(!cnp_persoana.matches("[0-9]+")){
            redirectAttributes.addFlashAttribute("errorMessageInfo", "CNP-ul trebuie să conțină doar cifre. ");
            return "redirect:/informatii";
        }

        // Verificam daca email-ul este valid
        if(!email_persoana.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")){
            redirectAttributes.addFlashAttribute("errorMessageInfo", "Email invalid. ");
            return "redirect:/register"; // Redirect back to the registration page
        }

        // Verificam daca numarul de telefon are 10 cifre si contine doar cifre
        if(telefon_persoana.length() != 10){
            redirectAttributes.addFlashAttribute("errorMessageInfo", "Numărul de telefon trebuie să conțină exact 10 cifre. ");
            return "redirect:/informatii";
        }
        else if(!telefon_persoana.matches("[0-9]+")){
            redirectAttributes.addFlashAttribute("errorMessageInfo", "Numărul de telefon trebuie să conțină doar cifre. ");
            return "redirect:/informatii";
        }

        //Verificam daca CNP-ul este unic in baza de date
        String sql = "SELECT COUNT(*) FROM \"PERSOANA\" WHERE \"CNP\" = ? AND \"ID_persoana\" != (SELECT \"ID_persoana\" FROM \"USERS\" WHERE username = ?)";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, cnp_persoana, username);
        if(count > 0){
            redirectAttributes.addFlashAttribute("errorMessageInfo", "CNP-ul introdus este deja folosit. ");
            return "redirect:/informatii";
        }

        // Conversia datei de nastere din String in java.sql.Date
        java.sql.Date data_nasterii_persoana_sql = java.sql.Date.valueOf(data_nasterii_persoana);

        // Daca utilizatorul nu exista, inseram datele in baza de date, altfel le actualizam
        if(!userExists) {
            String sql1 = "INSERT INTO \"PERSOANA\" (nume, prenume, \"CNP\", data_nasterii, sex, email, telefon) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql1,
                    nume_persoana,
                    prenume_persoana,
                    cnp_persoana,
                    data_nasterii_persoana_sql,
                    sex_persoana,
                    email_persoana,
                    telefon_persoana
            );

            // Updatam tablea USSERS cu ID-ul persoanei
            String sql2 = "UPDATE \"USERS\" SET \"ID_persoana\" = (SELECT \"p\".\"ID_persoana\" FROM \"PERSOANA\" \"p\" WHERE \"p\".\"CNP\" = ? LIMIT 1) WHERE \"username\" = ?";
            jdbcTemplate.update(sql2, cnp_persoana, username);
        }
        else {
            String sql1 = "UPDATE \"PERSOANA\" SET nume = ?, prenume = ?, \"CNP\" = ?, data_nasterii = ?, sex = ?, email = ?, telefon = ? " +
                    "WHERE \"ID_persoana\" = (SELECT \"USERS\".\"ID_persoana\" FROM \"USERS\" WHERE username = ?)";
            jdbcTemplate.update(sql1,
                    nume_persoana,
                    prenume_persoana,
                    cnp_persoana,
                    data_nasterii_persoana_sql,
                    sex_persoana,
                    email_persoana,
                    telefon_persoana,
                    username
            );
        }
        return "redirect:/informatii";
    }

    // Metoda pentru actualizarea adresei utilizatorului
    @PostMapping("/adresa")
    public String adresa(@RequestParam(name = "judet", required = false) String judet,
                         @RequestParam(name = "oras", required = false) String oras,
                         @RequestParam(name = "strada", required = false) String strada,
                         @RequestParam(name = "numar", required = false) String numar,
                         @RequestParam(name = "cod_postal", required = false) String cod_postal,
                         RedirectAttributes redirectAttributes) {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        // Verificam daca campurile din formular sunt goale
        if(judet.isEmpty() || oras.isEmpty() || strada.isEmpty() || numar.isEmpty() || cod_postal.isEmpty()){
            redirectAttributes.addFlashAttribute("errorMessageAdresa", "Toate câmpurile sunt obligatorii. ");
            return "redirect:/informatii";
        }

        // Verificam daca codul postal are 6 caractere si contine doar cifre
        if(cod_postal.length() != 6){
            redirectAttributes.addFlashAttribute("errorMessageAdresa", "Codul poștal trebuie să conțină exact 6 caractere. ");
            return "redirect:/informatii";
        }
        else if(!cod_postal.matches("[0-9]+")){
            redirectAttributes.addFlashAttribute("errorMessageAdresa", "Codul poștal trebuie să conțină doar cifre. ");
            return "redirect:/informatii";
        }

        // Daca adresa nu exista, inseram datele in baza de date, altfel le actualizam
        if(!adressExists) {
            // Cautam ID-ul persoanei in functie de username
            String sql1 = "SELECT \"ID_persoana\" FROM \"USERS\" WHERE \"username\" = ?";
            Integer idPersoana = jdbcTemplate.queryForObject(sql1, Integer.class, username);

            // Inseram datele adresei in baza de date folosind ID-ul persoanei
            String sql2 = "INSERT INTO \"ADRESA\" (\"ID_persoana\", judet, oras, strada, numarul, cod_postal) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql2, idPersoana, judet, oras, strada, numar, cod_postal);
        }
        else
        {
            String sql1 = "UPDATE \"ADRESA\" SET judet = ?, oras = ?, strada = ?, numarul = ?, cod_postal = ? " +
                    "WHERE \"ID_persoana\" = (SELECT \"USERS\".\"ID_persoana\" FROM \"USERS\" WHERE username = ?)";
            jdbcTemplate.update(sql1, judet, oras, strada, numar, cod_postal, username);
        }

        return "redirect:/informatii";
    }

}
