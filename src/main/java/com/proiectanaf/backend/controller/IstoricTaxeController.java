package com.proiectanaf.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

/**
 * Clasa pentru controllerul care se ocupa de vizualizarea taxelor platite de catre utilizator
 * @author Bolohan Marian-Cristian, 333AB
 * @version 4 Ianuarie 2025
 */

@Controller
public class IstoricTaxeController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/istoric_taxe")
    public String getIstoricTaxe(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);

        // Cauta taxele platite
        String sql1 = "SELECT \"TAXE\".\"ID_taxa\", \"TAXE\".\"tip_taxa\",\"TAXE\".\"suma_taxa\", \"TAXE\".\"data_platii\", \"TAXE\".\"platit\" FROM \"TAXE\" "
                + "INNER JOIN \"PERSOANA\" ON \"TAXE\".\"ID_persoana\" = \"PERSOANA\".\"ID_persoana\" "
                + "INNER JOIN \"USERS\" ON \"PERSOANA\".\"ID_persoana\" = \"USERS\".\"ID_persoana\" "
                + "WHERE \"USERS\".\"username\" = ? AND \"TAXE\".\"platit\" = 'da'";
        List<Map<String, Object>> dataList = jdbcTemplate.queryForList(sql1, username);
        model.addAttribute("dataList", dataList);
        System.out.println(dataList.isEmpty());

        return "istoric_taxe";
    }
}
