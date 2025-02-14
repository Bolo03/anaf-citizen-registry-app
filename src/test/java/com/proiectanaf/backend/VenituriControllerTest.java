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
public class VenituriControllerTest {

    @Autowired
    private MockMvc mockMvc; // Folosim MockMvc pentru a simula cereri HTTP

    @Test
    public void testVenituriPage() throws Exception {
        // Efectuăm o cerere GET la /venituri
        mockMvc.perform(MockMvcRequestBuilders.get("/venituri"))
                // Verificăm dacă statusul răspunsului este 200 (OK)
                .andExpect(status().isOk())
                // Verificăm dacă fișierul HTML returnat este venituri.html
                .andExpect(MockMvcResultMatchers.view().name("venituri"));
    }

    //Testam un mesaj de validare pentru venituri
    @Test
    public void testValidareInformatii() throws Exception {
        // Testam validarea campurilor goale
        mockMvc.perform(MockMvcRequestBuilders.post("/venituri")
                        .param("tip_bun", "")
                        .param("data_venit", "")
                        .param("suma_venit", ""))
                .andExpect(flash().attribute("errorMessage", "Toate campurile sunt obligatorii!"));
    }
}
