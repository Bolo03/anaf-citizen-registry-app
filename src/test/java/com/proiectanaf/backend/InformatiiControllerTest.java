package com.proiectanaf.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class InformatiiControllerTest {

    @Autowired
    private MockMvc mockMvc; // Folosim MockMvc pentru a simula cereri HTTP

    @Test
    public void testInformatiiPage() throws Exception {
        // Efectuăm o cerere GET la /informatii
        mockMvc.perform(MockMvcRequestBuilders.get("/informatii"))
                // Verificăm dacă statusul răspunsului este 200 (OK)
                .andExpect(status().isOk())
                // Verificăm dacă fișierul HTML returnat este informatii.html
                .andExpect(MockMvcResultMatchers.view().name("informatii"));
    }

    //Testam un mesaj de validare pentru informatii personale
    @Test
    public void testValidareInformatii() throws Exception {
        // Testam validarea campurilor goale
        mockMvc.perform(MockMvcRequestBuilders.post("/informatii")
                        .param("nume_persoana", "Popescu")
                        .param("prenume_persoana", "Ion")
                        .param("cnp_persoana", "")
                        .param("data_nasterii_persoana", "")
                        .param("sex_persoana", "M")
                        .param("email_persoana", "popescuIon@gmail.com")
                        .param("telefon_persoana", "0723456789"))
                .andExpect(flash().attribute("errorMessageInfo", "Toate câmpurile sunt obligatorii. "));

        // Testam format gresit email
        mockMvc.perform(MockMvcRequestBuilders.post("/informatii")
                        .param("nume_persoana", "Popescu")
                        .param("prenume_persoana", "Ion")
                        .param("cnp_persoana", "5010101099099")
                        .param("data_nasterii_persoana", "1999-01-01")
                        .param("sex_persoana", "M")
                        .param("email_persoana", "popescuIon")
                        .param("telefon_persoana", "0723456789"))
                .andExpect(flash().attribute("errorMessageInfo", "Email invalid. "));
    }
}
