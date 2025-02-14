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
 * Clasa pentru controllerul de vizualizare a persoanelor din baza de date de catre admin
 * Adminul poate sa vizualizeze usor persoanele din baza de date si sa vada toate detaliile despre acestea
 * cum ar fi adresa, veniturile, bunurile si conturile bancare ale acestora
 * @author Bolohan Marian-Cristian, 333AB
 * @version 3 Ianuarie 2025
 */

@Controller
public class VizualizarePersoaneController {
    private String tableName = "PERSOANA"; //numele tabelei din baza de date

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/vizualizare_persoane")
    public String vizualizarePersoane(Model model) {
        // Luam numele utilizatorului curent si il adaugam in model
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);

        // Luam numele coloanelor din tabela selectata
        String sql1 = "SELECT column_name FROM information_schema.columns WHERE table_name = '" + tableName + "'";
        List<Map<String, Object>> columnNamesPersoana = jdbcTemplate.queryForList(sql1);
        model.addAttribute("columnNamesPersoana", columnNamesPersoana);

        //daca adminul nu a selectat nicio persoana, afisam toate persoanele
        //altfel afisam detaliile despre persoana selectata
        if(!model.containsAttribute("detalii")) {

            String sql2 = "SELECT * FROM \"" + tableName + "\"";
            List<Map<String, Object>> dataListPersoana = jdbcTemplate.queryForList(sql2);

            model.addAttribute("dataListPersoana", dataListPersoana);
        }
        else
        {
            String detalii = (String) model.getAttribute("detalii");
            // luam ID-ul persoanei din atribut
            String[] detalii_split = detalii.split(",")[0].split("=");
            Integer id = Integer.valueOf(detalii_split[1]);

            //afisam persoana
            String sql3 = "SELECT * FROM \"" + tableName + "\" WHERE \"ID_persoana\" = ?";
            List<Map<String, Object>> dataListPersoana = jdbcTemplate.queryForList(sql3, id);
            model.addAttribute("dataListPersoana", dataListPersoana);

            //coloanele tabelei ADRESA
            String sql4 = "SELECT column_name FROM information_schema.columns WHERE table_name = 'ADRESA'";
            List<Map<String, Object>> columnNamesAdresa = jdbcTemplate.queryForList(sql4);
            model.addAttribute("columnNamesAdresa", columnNamesAdresa);

            //afisam adresa persoanei
            String sql5 = "SELECT * FROM \"ADRESA\" WHERE \"ID_persoana\" = ?";
            List<Map<String, Object>> dataListAdresa = jdbcTemplate.queryForList(sql5, id);
            model.addAttribute("dataListAdresa", dataListAdresa);

            //coloanele tabelei VENIT
            String sql6 = "SELECT column_name FROM information_schema.columns WHERE table_name = 'VENIT'";
            List<Map<String, Object>> columnNamesVenituri = jdbcTemplate.queryForList(sql6);
            model.addAttribute("columnNamesVenituri", columnNamesVenituri);

            //afisam veniturile persoanei
            String sql7 = "SELECT * FROM \"VENIT\" WHERE \"ID_persoana\" = ?";
            List<Map<String, Object>> dataListVenituri = jdbcTemplate.queryForList(sql7, id);
            model.addAttribute("dataListVenituri", dataListVenituri);

            //coloanele tabelei BUNURI
            String sql8 = "SELECT column_name FROM information_schema.columns WHERE table_name = 'BUNURI'";
            List<Map<String, Object>> columnNamesBunuri = jdbcTemplate.queryForList(sql8);
            model.addAttribute("columnNamesBunuri", columnNamesBunuri);

            //afisam bunurile persoanei
            String sql9 = "SELECT * FROM \"BUNURI\" WHERE \"ID_bun\" IN (SELECT \"ID_bun\" FROM \"PERSOANE_BUNURI\" WHERE \"ID_persoana\" = ?)";
            List<Map<String, Object>> dataListBunuri = jdbcTemplate.queryForList(sql9, id);
            model.addAttribute("dataListBunuri", dataListBunuri);

            //coloanele tabelei CONTURI_BANCARE
            String sql10 = "SELECT column_name FROM information_schema.columns WHERE table_name = 'CONTURI_BANCARE'";
            List<Map<String, Object>> columnNamesConturiBancare = jdbcTemplate.queryForList(sql10);
            model.addAttribute("columnNamesConturiBancare", columnNamesConturiBancare);

            //afisam conturile bancare ale persoanei
            String sql11 = "SELECT * FROM \"CONTURI_BANCARE\" WHERE \"ID_persoana\" = ?";
            List<Map<String, Object>> dataListConturiBancare = jdbcTemplate.queryForList(sql11, id);
            model.addAttribute("dataListConturiBancare", dataListConturiBancare);

        }

        return "vizualizare_persoane";
    }

    @PostMapping("/vizualizare_persoane")
    public String detaliiPersoana(@RequestParam("detalii") String detalii,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        // Luam numele utilizatorului curent si il adaugam in model
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);

        // redirectam detaliile persoanei pentru a afisa toate informatiile
        redirectAttributes.addFlashAttribute("detalii", detalii);

        return "redirect:/vizualizare_persoane";
    }
}
