package com.proiectanaf.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc; // Folosim MockMvc pentru a simula cereri HTTP

    @Test
    public void testLoginPage() throws Exception {
        // Efectuăm o cerere GET la /login
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                // Verificăm dacă statusul răspunsului este 200 (OK)
                .andExpect(MockMvcResultMatchers.status().isOk())
                // Verificăm dacă fișierul HTML returnat este "login" (adică login.html)
                .andExpect(MockMvcResultMatchers.view().name("login"));
    }

    @Test
    public void testLoginRedirect() throws Exception {
        // Login cu date valide
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", "admin")
                        .param("password", "admin"))
                // Verificăm dacă cererea duce la redirecționarea către /home
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));

        //Login esuat
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .param("username", "admin")
                        .param("password", "admin1"))
                // Verificăm dacă cererea duce la redirecționarea către /home
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/login"));
    }
}

