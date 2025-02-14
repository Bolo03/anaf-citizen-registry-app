package com.proiectanaf.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Clasa pentru controllerul de vizualizare a tabelelor din baza de date de catre admin
 * Aceasta clasa este folosita pentru a afisa continutul tabelelor din baza de date in mod vizual
 * @author Bolohan Marian-Cristian
 * @version 3 Ianuarie 2025
 */

@Controller
public class VizualizareTabeleController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/vizualizare_tabele")
    public String vizualizareTabele(@RequestParam(value = "table", required = false) String tableName,
                                    Model model) {
        // Luam numele utilizatorului curent si il adaugam in model
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);

        // Daca nicio tabela nu este selectata, afisam pagina de vizualizare a tabelelor
        if (tableName == null) {
            return "vizualizare_tabele"; // This will render the default home page (index)
        }

        // Verificam numele tabelelor
        List<String> allowedTables = Arrays.asList("USERS", "PERSOANA", "ADRESA", "VENIT", "BUNURI", "CONTURI_BANCARE", "PERSOANE_BUNURI", "TAXE");
        if (!allowedTables.contains(tableName)) {
            throw new IllegalArgumentException("Invalid table name");
        }

        // Luam numele coloanelor din tabela selectata
        String sql1 = "SELECT column_name FROM information_schema.columns WHERE table_name = '" + tableName + "'";
        List<Map<String, Object>> columnNames = jdbcTemplate.queryForList(sql1);

        // Luam datele din tabela selectata
        String sql2 = "SELECT * FROM \"" + tableName + "\"";
        List<Map<String, Object>> dataList = jdbcTemplate.queryForList(sql2);

        model.addAttribute("columnNames", columnNames);
        model.addAttribute("dataList", dataList);
        model.addAttribute("tableName", tableName);

        return "vizualizare_tabele";
    }
}
