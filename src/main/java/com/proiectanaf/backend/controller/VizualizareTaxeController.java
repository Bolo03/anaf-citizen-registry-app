package com.proiectanaf.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * Clasa pentru controllerul care se ocupa de vizualizarea taxelor platite sau neplatite
 * @author Bolohan Marian-Cristian, 333AB
 * @version 4 Ianuarie 2025
 */

@Controller
public class VizualizareTaxeController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/vizualizare_taxe")
    public String vizualizare_taxe(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);

        return "vizualizare_taxe";
    }

    @PostMapping("/vizualizare_taxe")
    public String vizualizare_taxe_post(@RequestParam(name="tip_taxa") String tip_taxa,
                                        Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);
        // Coloanele din tabela TAXE + PERSOANA
        if(tip_taxa != null) {
            model.addAttribute("tip_taxa", tip_taxa);

            // Cauta taxele platite sau neplatite
            String sql2 = "SELECT  \"TAXE\".\"ID_taxa\", \"PERSOANA\".\"nume\", \"PERSOANA\".\"prenume\", \"PERSOANA\".\"CNP\", " +
                    "\"TAXE\".\"tip_taxa\", \"TAXE\".\"suma_taxa\", \"TAXE\".\"data_scadenta\" ";
            if (tip_taxa.equals("da")) {
                sql2 += ", \"TAXE\".\"data_platii\" ";
            }
            sql2 += "FROM \"TAXE\" " +
                    "INNER JOIN \"PERSOANA\" ON \"TAXE\".\"ID_persoana\" = \"PERSOANA\".\"ID_persoana\" " +
                    "WHERE \"TAXE\".\"platit\" = ? " +
                    "ORDER BY \"TAXE\".\"ID_taxa\"";
            List<Map<String, Object>> dataList = jdbcTemplate.queryForList(sql2, tip_taxa);
            model.addAttribute("dataList", dataList);

            // preia toate cheile din dataList si salveaza in columnNames
            if(dataList.size() > 0) {
                model.addAttribute("columnNames", dataList.get(0).keySet());
            }

            System.out.println(model.getAttribute("dataList"));
        }
        return "vizualizare_taxe";
    }
}
