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
 * Clasa pentru contrllerul paginii de venituri a utilizatorului
 * Aceasta clasa contine metodele pentru afisarea veniturilor utilizatorului curent si pentru adaugarea, stergerea si modificarea veniturilor
 * @author Bolohan Marian-Cristian, 333AB
 * @version 8 Decembrie 2024
 */

@Controller
public class VenituriController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static boolean updateVenit = false; // initial inseram, nu updatam
    private static int id_to_update = -1;

    @GetMapping("/venituri")
    public String home(Model model) {
        // Resetare atribut pentru confirmarea stergerii
        if (!model.containsAttribute("deleteCheck")) {
            model.addAttribute("deleteCheck", 0);
        }

        // Luam username-ul utilizatorului curent pentru a-l aifsa in pagina
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);

        // Selectam toate veniturile utilizatorului curent si le afisam in pagina
        String sql = "SELECT * FROM \"VENIT\" WHERE \"ID_persoana\" = (SELECT \"USERS\".\"ID_persoana\" FROM \"USERS\" WHERE username = ?)" +
                     "ORDER BY \"data_venit\" DESC, \"suma_venit\" DESC, \"tip_venit\" ASC";
        List<Map<String, Object>> venituriList = jdbcTemplate.queryForList(sql, username);

        // Conversia date -> dd-mm-yyyy
        for(Map<String, Object> venit : venituriList) {
            String data_venit = venit.get("data_venit").toString();
            String[] data_venit_split = data_venit.split("-");
            String data_venit_final = data_venit_split[2] + "-" + data_venit_split[1] + "-" + data_venit_split[0];
            venit.put("data_venit", data_venit_final);

            // Conversia textului alte_venituri -> alte venituri
            String tip_venit = venit.get("tip_venit").toString();
            if(tip_venit.contains("_")) {
                String[] tip_venit_split = tip_venit.split("_");
                String tip_venit_final = "";
                for(String word : tip_venit_split) {
                    tip_venit_final += word + " ";
                }
                venit.put("tip_venit", tip_venit_final);
            }
        }

        boolean venituriExists = !venituriList.isEmpty();
        model.addAttribute("venituriExists", venituriExists);
        model.addAttribute("venituri", venituriExists ? venituriList : null);

        return "venituri";
    }

    // Metoda pentru validarea si adaugarea veniturilor in baza de date
    @PostMapping("/venituri")
    public String venituriPost(@RequestParam(value = "tip_venit", required = false) String tip_venit,
                               @RequestParam(value = "suma_venit", required = false) String suma_venit,
                               @RequestParam(value = "data_venit", required = false) String data_venit,
                               Model model,
                               RedirectAttributes redirectAttributes) {

        // Luam username-ul utilizatorului curent pentru a-l aifsa in pagina
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);

        // Verificam daca toate campurile din formular sunt completate
        if(tip_venit == null || suma_venit == null || data_venit == null ||
            tip_venit.isEmpty() || suma_venit.isEmpty() || data_venit.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Toate campurile sunt obligatorii!");
            return "redirect:/venituri";
        }

        // conversia datei
        java.sql.Date data_venit_sql = java.sql.Date.valueOf(data_venit);
        // conversia sumei in double
        double suma_venit_double = Double.parseDouble(suma_venit);

        // daca nu se face update, se insereaza un nou venit
        if(!updateVenit) {
            String sql1 = "INSERT INTO \"VENIT\" (\"tip_venit\", \"suma_venit\", \"data_venit\", \"ID_persoana\") " +
                          "VALUES (?, ?, ?, (SELECT \"USERS\".\"ID_persoana\" FROM \"USERS\" WHERE username = ?))";

            jdbcTemplate.update(sql1, tip_venit, suma_venit_double, data_venit_sql, username);
        }
        else
        {
            String sql3 = "UPDATE \"VENIT\" SET \"tip_venit\" = ?, \"suma_venit\" = ?, \"data_venit\" = ? WHERE \"ID_venit\" = ?";
            jdbcTemplate.update(sql3, tip_venit, suma_venit_double, data_venit_sql, id_to_update);
            updateVenit = false;
            id_to_update = -1;
        }

        return "redirect:/venituri";
    }

    // Metoda pentru stergerea veniturilor din baza de date
    @PostMapping("/venituri/delete/")
    public String venituriDelete(@RequestParam(value = "id_venit", required = false) String id,
                                 @RequestParam(required = false) String action,
                                 RedirectAttributes redirectAttributes) {

        // Se afiseaza un mesaj de confirmare inainte de stergere
        if ("check".equals(action)) {
            redirectAttributes.addFlashAttribute("deleteCheck", 1);
            redirectAttributes.addFlashAttribute("id_venit", id);
            return "redirect:/venituri";
        }

        // Daca se confirma stergerea, se sterge venitul
        if("da".equals(action)) {
            Integer numericId = Integer.parseInt(id);

            String sql = "DELETE FROM \"VENIT\" WHERE \"ID_venit\" = ? ";
            jdbcTemplate.update(sql, numericId);

            redirectAttributes.addFlashAttribute("deleteCheck", 0);
        }
        else if("nu".equals(action)) {
            redirectAttributes.addFlashAttribute("deleteCheck", 0);
        }

        return "redirect:/venituri";
    }

    // Metoda pentru update venituri
    @GetMapping("/venituri/update/")
    public String venituriUpdate(@RequestParam("id_venit") String id, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        model.addAttribute("username", username);
        updateVenit = true;

        Integer numericId = Integer.parseInt(id);

        String sql1 = "SELECT * FROM \"VENIT\" WHERE \"ID_venit\" = ?";
        Map<String, Object> venit = jdbcTemplate.queryForMap(sql1, numericId);

        model.addAttribute("tip_venit", venit.get("tip_venit"));
        model.addAttribute("suma_venit", venit.get("suma_venit"));
        model.addAttribute("data_venit", venit.get("data_venit"));

        id_to_update = numericId;

        return "venituri";
    }

}
