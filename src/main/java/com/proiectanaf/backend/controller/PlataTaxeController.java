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

@Controller
public class PlataTaxeController {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/plata_taxe")
    public String plata_taxe(Model model) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);

        String sql1 = "SELECT \"TAXE\".\"ID_taxa\", \"TAXE\".\"tip_taxa\",\"TAXE\".\"suma_taxa\", \"TAXE\".\"data_scadenta\", \"TAXE\".\"platit\" FROM \"TAXE\" "
                + "INNER JOIN \"PERSOANA\" ON \"TAXE\".\"ID_persoana\" = \"PERSOANA\".\"ID_persoana\" "
                + "INNER JOIN \"USERS\" ON \"PERSOANA\".\"ID_persoana\" = \"USERS\".\"ID_persoana\" "
                + "WHERE \"USERS\".\"username\" = ? AND \"TAXE\".\"platit\" = 'nu'";
        List<Map<String, Object>> dataList = jdbcTemplate.queryForList(sql1, username);
        model.addAttribute("dataList", dataList);
        System.out.println(dataList.isEmpty());

        return "plata_taxe";
    }

    @PostMapping("/plata_taxe")
    public String plata_taxe_post(@RequestParam(name="id", required = false) String id,
                                  @RequestParam(name="id_cont", required = false) String id_cont,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);

        String sql1 = "SELECT \"TAXE\".\"ID_taxa\", \"TAXE\".\"tip_taxa\",\"TAXE\".\"suma_taxa\", \"TAXE\".\"data_scadenta\", \"TAXE\".\"platit\" FROM \"TAXE\" "
                + "INNER JOIN \"PERSOANA\" ON \"TAXE\".\"ID_persoana\" = \"PERSOANA\".\"ID_persoana\" "
                + "INNER JOIN \"USERS\" ON \"PERSOANA\".\"ID_persoana\" = \"USERS\".\"ID_persoana\" "
                + "WHERE \"USERS\".\"username\" = ? AND \"TAXE\".\"platit\" = 'nu'";
        List<Map<String, Object>> dataList = jdbcTemplate.queryForList(sql1, username);
        model.addAttribute("dataList", dataList);

        if(id != null) {
            Integer plata_taxa = 1;
            model.addAttribute("plata_taxa", plata_taxa);
            model.addAttribute("id_taxa", Integer.parseInt(id));

            //Informatii despre taxa
            String sql2 = "SELECT \"ID_taxa\", \"tip_taxa\", \"suma_taxa\", \"data_scadenta\" FROM \"TAXE\" WHERE \"ID_taxa\" = ?";
            Map<String, Object> dataListTaxa = jdbcTemplate.queryForMap(sql2, Integer.parseInt(id));
            System.out.println(dataListTaxa);
            model.addAttribute("dataListTaxa", dataListTaxa);


            //Conturi bancare cu care se poate face plata
            String sql3 = "SELECT \"ID_cont\", \"IBAN\", \"tip_cont\" FROM \"CONTURI_BANCARE\" "
                    + "INNER JOIN \"PERSOANA\" ON \"CONTURI_BANCARE\".\"ID_persoana\" = \"PERSOANA\".\"ID_persoana\" "
                    + "INNER JOIN \"USERS\" ON \"PERSOANA\".\"ID_persoana\" = \"USERS\".\"ID_persoana\" "
                    + "WHERE \"USERS\".\"username\" = ? AND \"CONTURI_BANCARE\".\"tip_cont\" != 'economii'"
                    + "ORDER BY \"CONTURI_BANCARE\".\"tip_cont\" DESC";
            List<Map<String, Object>> dataListConturi = jdbcTemplate.queryForList(sql3, username);
            model.addAttribute("dataListConturi", dataListConturi);
        }

        if(id_cont != null) {
            Integer plata_taxa = 0;
            model.addAttribute("plata_taxa", plata_taxa);

            //Data de azi
            java.util.Date date = new java.util.Date();
            java.sql.Date data_azi = new java.sql.Date(date.getTime());

            //Actualizare in baza de date
            String sql4 = "UPDATE \"TAXE\" SET \"platit\" = 'da', \"data_platii\" = ? WHERE \"ID_taxa\" = ?";
            jdbcTemplate.update(sql4, data_azi, Integer.parseInt(id));

            redirectAttributes.addFlashAttribute("mesaj", "Plata a fost efectuata cu succes!");

            return "redirect:/plata_taxe";
        }



        return "plata_taxe";
    }
}
