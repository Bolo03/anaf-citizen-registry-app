package com.proiectanaf.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

/**
 * Clasa pentru contrllerul paginii de conturi bancare a utilizatorului
 * Aceasta clasa contine metodele pentru afisarea conturilor bancare ale utilizatorului curent si pentru adaugarea, stergerea si modificarea conturilor bancare
 * @author Bolohan Marian-Cristian, 333AB
 * @version 8 Decembrie 2024
 */

@Controller
public class ConturiBancareController {

    private static boolean updateCont = false; // initial inseram, nu updatam
    private static int id_to_update = -1;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/conturi_bancare")
    public String home(Model model) {
        // Resetare atribut pentru confirmarea stergerii
        if (!model.containsAttribute("deleteCheck")) {
            model.addAttribute("deleteCheck", 0);
        }

        // Luam username-ul utilizatorului curent pentru a-l aifsa in pagina
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);

        // Selectam toate conturile bancare ale utilizatorului curent si le afisam in pagina
        String sql = "SELECT * FROM \"CONTURI_BANCARE\" WHERE \"ID_persoana\" = (SELECT \"USERS\".\"ID_persoana\" FROM \"USERS\" WHERE username = ?)";
        List<Map<String, Object>> venituriList = jdbcTemplate.queryForList(sql, username);

        // conversia alte_conturi -> alte conturi
        for(Map<String, Object> cont : venituriList) {
            String tip_cont = cont.get("tip_cont").toString();
            if(tip_cont.contains("_")) {
                String[] tip_cont_split = tip_cont.split("_");
                String tip_cont_final = "";
                for(String word : tip_cont_split) {
                    tip_cont_final += word + " ";
                }
                cont.put("tip_cont", tip_cont_final);
            }
        }

        boolean conturiExists = !venituriList.isEmpty();
        model.addAttribute("conturiExists", conturiExists);
        model.addAttribute("conturi", conturiExists ? venituriList : null);

        return "conturi_bancare";
    }

    // Metoda pentru validarea si adaugarea unui cont bancar
    @PostMapping("/conturi_bancare")
    public String homePost(@RequestParam(value = "IBAN", required = false) String IBAN,
                           @RequestParam(value = "banca", required = false) String banca,
                           @RequestParam(value = "tip_cont", required = false) String tip_cont,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        // Luam username-ul utilizatorului curent pentru a-l aifsa in pagina
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);

        //Verificam daca toate campurile sunt completate
        if(IBAN == null || banca == null || tip_cont == null ||
           IBAN.isEmpty() || banca.isEmpty() || tip_cont.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Toate campurile sunt obligatorii!");
            return "redirect:/conturi_bancare";
        }

        //Verificam daca IBAN-ul are 24 de caractere
        if(IBAN.length() != 24) {
            redirectAttributes.addFlashAttribute("errorMessage", "IBAN-ul trebuie sa aiba 24 de caractere!");
            return "redirect:/conturi_bancare";
        }

        // daca nu se face update, se insereaza un nou venit
        if(!updateCont)
        {
            //Verificam daca IBAN-ul este unic in baza de date
            String sql1 = "SELECT COUNT(*) FROM \"CONTURI_BANCARE\" WHERE \"IBAN\" = ?";
            Integer count = jdbcTemplate.queryForObject(sql1, Integer.class, IBAN);
            //Daca IBAN-ul nu este unic, afisam un mesaj de eroare
            if(count > 0) {
                redirectAttributes.addFlashAttribute("errorMessage", "IBAN-ul trebuie sa fie unic!");
                return "redirect:/conturi_bancare";
            }

            // Inseram noul cont bancar in baza de date
            String sql = "INSERT INTO \"CONTURI_BANCARE\" (\"IBAN\", \"banca\", \"tip_cont\", \"ID_persoana\") " +
                         "VALUES (?, ?, ?, (SELECT \"ID_persoana\" FROM \"USERS\" WHERE username = ?))";
            jdbcTemplate.update(sql, IBAN, banca, tip_cont, username);
        }
        else
        {
            String sql = "UPDATE \"CONTURI_BANCARE\" SET \"IBAN\" = ?, \"banca\" = ?, \"tip_cont\" = ? " +
                         "WHERE \"ID_cont\" = ?";
            jdbcTemplate.update(sql, IBAN, banca, tip_cont, id_to_update);
            updateCont = false;
            id_to_update = -1;
        }

        return "redirect:/conturi_bancare";
    }

    // Metoda pentru stergerea unui cont bancar
    @PostMapping("conturi_bancare/delete/")
    public String deleteCont(@RequestParam(name = "Id_cont", required = false) String id,
                             @RequestParam(required = false) String action,
                             RedirectAttributes redirectAttributes) {

        // Se afiseaza un mesaj de confirmare inainte de stergere
        if ("check".equals(action)) {
            // Show the confirmation modal with the specified id
            redirectAttributes.addFlashAttribute("deleteCheck", 1);
            redirectAttributes.addFlashAttribute("Id_cont", id);
            return "redirect:/conturi_bancare";
        }

        // Daca se confirma stergerea, se sterge contul bancar
        if("da".equals(action)) {
            Integer numericId = Integer.parseInt(id);

            String sql = "DELETE FROM \"CONTURI_BANCARE\" WHERE \"ID_cont\" = ?";
            jdbcTemplate.update(sql, numericId);

            redirectAttributes.addFlashAttribute("deleteCheck", 0);
        }
        else if("nu".equals(action)) {
            redirectAttributes.addFlashAttribute("deleteCheck", 0);
        }

        return "redirect:/conturi_bancare";
    }

    // Metoda pentru update conturi bancare
    @GetMapping("conturi_bancare/update/")
    public String updateCont(@RequestParam("Id_cont") String id, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        model.addAttribute("username", username);
        updateCont = true;
        Integer numericId = Integer.parseInt(id);

        String sql = "SELECT * FROM \"CONTURI_BANCARE\" WHERE \"ID_cont\" = ?";
        Map<String, Object> cont = jdbcTemplate.queryForMap(sql, numericId);

        model.addAttribute("IBAN", cont.get("IBAN"));
        model.addAttribute("banca", cont.get("banca"));
        model.addAttribute("tip_cont", cont.get("tip_cont"));

        id_to_update = numericId;

        return "conturi_bancare";
    }
}
