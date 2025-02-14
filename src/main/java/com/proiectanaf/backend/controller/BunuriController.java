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
 * Clasa pentru contrllerul paginii de bunuri a utilizatorului
 * Aceasta clasa contine metodele pentru afisarea bunurilor utilizatorului curent si pentru adaugarea, stergerea si modificarea bunurilor
 * @author Bolohan Marian-Cristian, 333AB
 * @version 8 Decembrie 2024
 */

@Controller
public class BunuriController {

    private static boolean updateBun = false; // initial inseram, nu updatam
    private static int id_to_update = -1;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/bunuri")
    public String home(Model model) {
        // Resetare atribut pentru confirmarea stergerii
        if (!model.containsAttribute("deleteCheck")) {
            model.addAttribute("deleteCheck", 0);
        }

        // Luam username-ul utilizatorului curent pentru a-l aifsa in pagina
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);

        // Selectam toate bunurile utilizatorului curent si le afisam in pagina
        String sql = "SELECT * FROM \"BUNURI\" " +
                     "WHERE \"ID_bun\" IN " +
                     "(SELECT \"ID_bun\" FROM \"PERSOANE_BUNURI\" " +
                     "INNER JOIN \"PERSOANA\" ON \"PERSOANA\".\"ID_persoana\" = \"PERSOANE_BUNURI\".\"ID_persoana\" " +
                     "INNER JOIN \"USERS\" ON \"USERS\".\"ID_persoana\" = \"PERSOANA\".\"ID_persoana\" " +
                     "WHERE username = ?)" +
                     "ORDER BY \"data_achizitiei\" DESC, \"valoare\" DESC, \"tip_bun\" ASC";
        List<Map<String, Object>> bunuriList = jdbcTemplate.queryForList(sql, username);

        //conversia date -> dd-mm-yyyy
        for(Map<String, Object> bun : bunuriList) {
            String data_achizitiei = bun.get("data_achizitiei").toString();
            String[] data_achizitiei_split = data_achizitiei.split("-");
            String data_achizitiei_final = data_achizitiei_split[2] + "-" + data_achizitiei_split[1] + "-" + data_achizitiei_split[0];
            bun.put("data_achizitiei", data_achizitiei_final);

            //conversia textului tip_bun -> tip bun
            String tip_bun = bun.get("tip_bun").toString();
            if(tip_bun.contains("_")) {
                String[] tip_bun_split = tip_bun.split("_");
                String tip_bun_final = "";
                for(String word : tip_bun_split) {
                    tip_bun_final += word + " ";
                }
                bun.put("tip_bun", tip_bun_final);
            }
        }

        boolean bunuriExists = !bunuriList.isEmpty();
        model.addAttribute("bunuriExists", bunuriExists);
        model.addAttribute("bunuri", bunuriExists ? bunuriList : null);

        return "bunuri";
    }

    // Metoda pentru validarea si adaugarea bunurilor in baza de date
    @PostMapping("/bunuri")
    public String homePost(@RequestParam(value = "tip_bun", required = false) String tip_bun,
                           @RequestParam(value = "valoare", required = false) String valoare,
                           @RequestParam(value = "data_achizitiei", required = false) String data_achizitiei,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        // Luam username-ul utilizatorului curent pentru a-l aifsa in pagina
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);

        // Verificam daca toate campurile sunt completate
        if(tip_bun == null || valoare == null || data_achizitiei == null ||
            tip_bun.isEmpty() || valoare.isEmpty() || data_achizitiei.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Toate campurile sunt obligatorii!");
            return "redirect:/bunuri";
        }

        // conversia datei in formatul sql
        java.sql.Date data_achizitiei_sql = java.sql.Date.valueOf(data_achizitiei);
        // conversia valorii in double
        double valoare_double = Double.parseDouble(valoare);

        // daca nu updatam, inseram in baza de date
        if(!updateBun) {
            String sql = "SELECT COUNT(*) FROM \"BUNURI\" WHERE \"tip_bun\" = ? AND \"valoare\" = ? AND \"data_achizitiei\" = ?";
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tip_bun, valoare_double, data_achizitiei_sql);

            if(count == 0) {
                String sql1 = "INSERT INTO \"BUNURI\" (\"tip_bun\", \"valoare\", \"data_achizitiei\") VALUES (?, ?, ?)";
                jdbcTemplate.update(sql1, tip_bun, valoare_double, data_achizitiei_sql);
            }

            // Inseram legatura intre persoana si bun in tabela de legatura
            String sql2 = "INSERT INTO \"PERSOANE_BUNURI\" (\"ID_persoana\", \"ID_bun\") " +
                          "VALUES ((SELECT \"USERS\".\"ID_persoana\" FROM \"USERS\" WHERE username = ?), " +
                          "(SELECT \"BUNURI\".\"ID_bun\" FROM \"BUNURI\" WHERE \"tip_bun\" = ? AND \"valoare\" = ? AND \"data_achizitiei\" = ?))";
            jdbcTemplate.update(sql2, username, tip_bun, valoare_double, data_achizitiei_sql);

        }
        else
        {
            String sql = "UPDATE \"BUNURI\" SET \"tip_bun\" = ?, \"valoare\" = ?, \"data_achizitiei\" = ? WHERE \"ID_bun\" = ?";
            jdbcTemplate.update(sql, tip_bun, valoare_double, data_achizitiei_sql, id_to_update);
            updateBun = false;
            id_to_update = -1;
        }

        return "redirect:/bunuri";
    }

    // Metoda pentru stergerea bunurilor din baza de date
    @PostMapping("/bunuri/delete/")
    public String bunuriDelete(@RequestParam("ID_bun") String id,
                               @RequestParam(required = false) String action,
                               RedirectAttributes redirectAttributes) {

        // Se afiseaza un mesaj de confirmare inainte de stergere
        if ("check".equals(action)) {
            redirectAttributes.addFlashAttribute("deleteCheck", 1);
            redirectAttributes.addFlashAttribute("ID_bun", id);
            return "redirect:/bunuri";
        }

        // Daca se confirma stergerea, se sterge contul bancar
        if("da".equals(action)){
        Integer numericId = Integer.parseInt(id);

        String sql2 = "DELETE FROM \"PERSOANE_BUNURI\" WHERE \"ID_bun\" = ?";
        jdbcTemplate.update(sql2, numericId);

        String sql1 = "DELETE FROM \"BUNURI\" WHERE \"ID_bun\" = ?";
        jdbcTemplate.update(sql1, numericId);

        redirectAttributes.addFlashAttribute("deleteCheck", 0);
        }
        else if("nu".equals(action)) {
            redirectAttributes.addFlashAttribute("deleteCheck", 0);
        }

        return "redirect:/bunuri";
    }

    // Metoda pentru update bunuri
    @GetMapping("/bunuri/update/")
    public String bunuriUpdate(@RequestParam("ID_bun") String id, Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        model.addAttribute("username", username);
        updateBun = true;

        Integer numericId = Integer.parseInt(id);

        String sql = "SELECT * FROM \"BUNURI\" WHERE \"ID_bun\" = ?";
        Map<String, Object> bun = jdbcTemplate.queryForMap(sql, numericId);

        model.addAttribute("tip_bun", bun.get("tip_bun"));
        model.addAttribute("valoare", bun.get("valoare"));
        model.addAttribute("data_achizitiei", bun.get("data_achizitiei"));
        model.addAttribute("bun", bun);

        id_to_update = numericId;

        return "bunuri";
    }
}
