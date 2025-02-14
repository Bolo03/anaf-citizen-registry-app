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
public class ConturiBancareControllerTest {

    @Autowired
    private MockMvc mockMvc; // Folosim MockMvc pentru a simula cereri HTTP

    @Test
    public void testConturiBancarePage() throws Exception {
        // Efectuăm o cerere GET la /conturi_bancare
        mockMvc.perform(MockMvcRequestBuilders.get("/conturi_bancare"))
                // Verificăm dacă statusul răspunsului este 200 (OK)
                .andExpect(status().isOk())
                // Verificăm dacă fișierul HTML returnat este conturi_bancare.html
                .andExpect(MockMvcResultMatchers.view().name("conturi_bancare"));
    }

    //Testam un mesaj de validare pentru venituri
    @Test
    public void testValidareInformatii() throws Exception {
        // Testam validarea campurilor goale
        mockMvc.perform(MockMvcRequestBuilders.post("/conturi_bancare")
                        .param("tip_cont", "")
                        .param("IBAN", "")
                        .param("banca", ""))
                .andExpect(flash().attribute("errorMessage", "Toate campurile sunt obligatorii!"));
    }
}
