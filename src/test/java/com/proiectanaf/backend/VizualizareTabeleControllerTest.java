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
public class VizualizareTabeleControllerTest {

    @Autowired
    private MockMvc mockMvc; // Folosim MockMvc pentru a simula cereri HTTP

    @Test
    public void testVizualizareTabelePage() throws Exception {
        // Efectuăm o cerere GET la /vizualizare_tabele
        mockMvc.perform(MockMvcRequestBuilders.get("/vizualizare_tabele"))
                // Verificăm dacă statusul răspunsului este 200 (OK)
                .andExpect(status().isOk())
                // Verificăm dacă fișierul HTML returnat este vizualizare_tabele.html
                .andExpect(MockMvcResultMatchers.view().name("vizualizare_tabele"));
    }
}
