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
 * Clasa pentru controllerul care se ocupa de inserarea taxelor de catre admin
 * Adminul poate sa adauge taxe pentru persoanele din baza de date
 * Acest lucru este realizat printr-un formaular care se updateaza dinamic in functie de optiunile selectate
 * Adminul poate sa adauge taxe pentru venituri, cladiri, terenuri si autoturisme
 * @author Bolohan Marian-Cristian, 333AB
 * @version 3 Ianuarie 2025
 */

@Controller
public class TaxeController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Functie pentru calcularea impozitului si a datei de scadenta (ultima zi a anului curent)
    private void calculeazaImpozitDataScadenta(Float suma_venit_float, Model model)
    {
        Double suma_venit = suma_venit_float.doubleValue();  // Convert to Double

        // Calcul impozit
        Double impozit = suma_venit * 0.1; // 10% impozit
        model.addAttribute("suma_impozit", impozit);

        // ultima zi a anului curent
        java.time.LocalDate localDate = java.time.LocalDate.now();
        java.time.LocalDate lastDayOfYear = localDate.withMonth(12).withDayOfMonth(31);

        // converie LocalDate -> java.sql.Date
        java.sql.Date data_scadenta = java.sql.Date.valueOf(lastDayOfYear);

        model.addAttribute("data_scadenta", data_scadenta);
    }

    @GetMapping("/taxe")
    public String taxe(Model model) {
        // Luam numele utilizatorului curent si il adaugam in model
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);

        // cauta toate persoanele
        String sql1 = "SELECT \"ID_persoana\" ,\"nume\", \"prenume\" FROM \"PERSOANA\" ORDER BY \"nume\", \"prenume\"";
        List<Map<String, Object>> persoane = jdbcTemplate.queryForList(sql1);
        model.addAttribute("persoane", persoane);

        return "taxe";
    }

    @PostMapping("/taxe")
    public String taxePost(@RequestParam(name="persoana", required = false) String persoana,
                           @RequestParam(name="tip_taxa", required = false) String tip_taxa,
                           @RequestParam(name="venit", required = false) String venit,
                           @RequestParam(name="cladire", required = false) String cladire,
                           @RequestParam(name="teren", required = false) String teren,
                           @RequestParam(name="autoturism", required = false) String autoturism,
                           @RequestParam(name="suma_impozit", required = false) String suma_impozit,
                            @RequestParam(name="data_scadenta", required = false) String data_scadenta,
                           Model model,
                           RedirectAttributes redirectAttributes) {

        // Luam numele utilizatorului curent si il adaugam in model
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("username", username);

        Integer id_persoana = null;

        // cauta toate persoanele
        String sql1 = "SELECT \"ID_persoana\" ,\"nume\", \"prenume\" FROM \"PERSOANA\" ORDER BY \"nume\", \"prenume\"";
        List<Map<String, Object>> persoane = jdbcTemplate.queryForList(sql1);
        model.addAttribute("persoane", persoane);

        // semnalizeaza persoana selectata, afiseaza select pentru tipul taxei
        if(persoana != null && !persoana.isEmpty()) {
            id_persoana = Integer.valueOf(persoana);
            model.addAttribute("selectedPersonId", id_persoana);
        }

        // semnalizeaza tipul taxei selectat, afiseaza select pentru venituri, cladiri, terenuri, autoturisme
        if(tip_taxa != null && !tip_taxa.isEmpty()) {
            model.addAttribute("selectedTaxType", tip_taxa);

            // afiseaza venitul de impozitat
            if(tip_taxa.equals("impozit_venit")) {
                String sql = "SELECT \"ID_venit\", \"tip_venit\", \"suma_venit\", \"data_venit\" FROM \"VENIT\" WHERE \"ID_persoana\" = ?";
                List<Map<String, Object>> venituri = jdbcTemplate.queryForList(sql, id_persoana);
                model.addAttribute("venituri", venituri);
            }
            // afiseaza cladirea de impozitat
            if(tip_taxa.equals("impozit_cladire")) {
                String sql = "SELECT \"BUNURI\".\"ID_bun\", \"BUNURI\".\"tip_bun\", \"BUNURI\".\"valoare\" FROM \"BUNURI\" " +
                        "INNER JOIN \"PERSOANE_BUNURI\" ON \"BUNURI\".\"ID_bun\" = \"PERSOANE_BUNURI\".\"ID_bun\" " +
                        "WHERE \"PERSOANE_BUNURI\".\"ID_persoana\" = ? AND (\"BUNURI\".\"tip_bun\" = 'casa' OR \"BUNURI\".\"tip_bun\" = 'apartament');";
                List<Map<String, Object>> cladiri = jdbcTemplate.queryForList(sql, id_persoana);
                model.addAttribute("cladiri", cladiri);

            }
            // afiseaza terenul de impozitat
            if(tip_taxa.equals("impozit_teren")) {
//                String sql = "SELECT \"BUNURI\".\"ID_bun\", \"BUNURI\".\"tip_bun\", \"BUNURI\".\"valoare\" FROM \"BUNURI\" " +
//                        "INNER JOIN \"PERSOANE_BUNURI\" ON \"BUNURI\".\"ID_bun\" = \"PERSOANE_BUNURI\".\"ID_bun\" " +
//                        "WHERE \"PERSOANE_BUNURI\".\"ID_persoana\" = ? AND \"BUNURI\".\"tip_bun\" = 'teren';";
                String sql = "SELECT \"BUNURI\".\"ID_bun\", \"BUNURI\".\"tip_bun\", \"BUNURI\".\"valoare\" FROM \"BUNURI\" " +
                        "WHERE \"BUNURI\".\"ID_bun\" IN (SELECT \"ID_bun\" FROM \"PERSOANE_BUNURI\" WHERE \"ID_persoana\" = ?) AND \"BUNURI\".\"tip_bun\" = 'teren';";
                List<Map<String, Object>> terenuri = jdbcTemplate.queryForList(sql, id_persoana);
                model.addAttribute("terenuri", terenuri);
            }
            // afiseaza autoturismul de impozitat
            if(tip_taxa.equals("impozit_auto")) {
//                String sql = "SELECT \"BUNURI\".\"ID_bun\", \"BUNURI\".\"tip_bun\", \"BUNURI\".\"valoare\" FROM \"BUNURI\" " +
//                        "INNER JOIN \"PERSOANE_BUNURI\" ON \"BUNURI\".\"ID_bun\" = \"PERSOANE_BUNURI\".\"ID_bun\" " +
//                        "WHERE \"PERSOANE_BUNURI\".\"ID_persoana\" = ? AND \"BUNURI\".\"tip_bun\" = 'autoturism';";
                String sql = "SELECT \"BUNURI\".\"ID_bun\", \"BUNURI\".\"tip_bun\", \"BUNURI\".\"valoare\" FROM \"BUNURI\" " +
                        "WHERE \"BUNURI\".\"ID_bun\" IN (SELECT \"ID_bun\" FROM \"PERSOANE_BUNURI\" WHERE \"ID_persoana\" = ?) AND \"BUNURI\".\"tip_bun\" = 'autoturism';";
                List<Map<String, Object>> masini = jdbcTemplate.queryForList(sql, id_persoana);
                model.addAttribute("autoturisme", masini);
            }
        }

        // semnalizeaza venitul selectat, afiseaza suma impozitului si data scadenta
        if(venit != null && !venit.isEmpty()) {
            model.addAttribute("selectedIncomeId", Integer.valueOf(venit));

            String sql = "SELECT \"suma_venit\" FROM \"VENIT\" WHERE \"ID_venit\" = ?";
            List<Map<String, Object>> venituri = jdbcTemplate.queryForList(sql, Integer.valueOf(venit));
            //calculeaza impozitul
            Float suma_venit_float = (Float) venituri.get(0).get("suma_venit");
            calculeazaImpozitDataScadenta(suma_venit_float, model);

        }

        // semnalizeaza cladirea selectata, afiseaza suma impozitului si data scadenta
        if(cladire != null && !cladire.isEmpty()) {
            model.addAttribute("selectedBuildingId", Integer.valueOf(cladire));
            System.out.println(model.getAttribute("selectedBuildingId"));

            String sql = "SELECT \"valoare\" FROM \"BUNURI\" WHERE \"ID_bun\" = ?";
            List<Map<String, Object>> cladiri = jdbcTemplate.queryForList(sql, Integer.valueOf(cladire));
            //calculeaza impozitul
            Float suma_cladire_float = (Float) cladiri.get(0).get("valoare");
            calculeazaImpozitDataScadenta(suma_cladire_float, model);
        }

        // semnalizeaza terenul selectat, afiseaza suma impozitului si data scadenta
        if(teren != null && !teren.isEmpty()) {
            model.addAttribute("selectedLandId", Integer.valueOf(teren));
            System.out.println(model.getAttribute("selectedLandId"));

            String sql = "SELECT \"valoare\" FROM \"BUNURI\" WHERE \"ID_bun\" = ?";
            List<Map<String, Object>> terenuri = jdbcTemplate.queryForList(sql, Integer.valueOf(teren));
            //calculeaza impozitul
            Float suma_teren_float = (Float) terenuri.get(0).get("valoare");
            calculeazaImpozitDataScadenta(suma_teren_float, model);
        }

        // semnalizeaza autoturismul selectat, afiseaza suma impozitului si data scadenta
        if(autoturism != null && !autoturism.isEmpty()) {
            model.addAttribute("selectedCarId", Integer.valueOf(autoturism));

            String sql = "SELECT \"valoare\" FROM \"BUNURI\" WHERE \"ID_bun\" = ?";
            List<Map<String, Object>> masini = jdbcTemplate.queryForList(sql, Integer.valueOf(autoturism));
            //calculeaza impozitul
            Float suma_masina_float = (Float) masini.get(0).get("valoare");
            calculeazaImpozitDataScadenta(suma_masina_float, model);
        }

        // adauga taxa in baza de date
        if(suma_impozit != null && !suma_impozit.isEmpty() && data_scadenta != null && !data_scadenta.isEmpty()) {

            String sql = "INSERT INTO \"TAXE\" (\"ID_persoana\", \"tip_taxa\", \"suma_taxa\", \"data_scadenta\") VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sql, id_persoana, tip_taxa, Float.valueOf(suma_impozit), java.sql.Date.valueOf(data_scadenta));

            redirectAttributes.addFlashAttribute("errorMessage", "Taxa adaugata cu succes!");

            return "redirect:/taxe";
        }

        return "taxe";
    }
}
