package com.proiectanaf.backend;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc; // Folosim MockMvc pentru a simula cereri HTTP

    @Test
    public void testRegisterPage() throws Exception {
        // Efectuăm o cerere GET la /register
        mockMvc.perform(MockMvcRequestBuilders.post("/register"))
                // Verificăm dacă statusul răspunsului este 200 (OK)
                .andExpect(status().isOk())
                // Verificăm dacă fișierul HTML returnat este "register" (adică register.html)
                .andExpect(MockMvcResultMatchers.view().name("register"));
    }

    @Test
    public void testValidRegistration() throws Exception {
        // Teste validari parole greșite
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("username", "user10")
                        .param("password", "user10")
                        .param("email", "user10@user.com"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("errorMessage", "Parola este prea scurtă. "));

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("username", "user10")
                        .param("password", "!user123")
                        .param("email", "user10@user.com"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("errorMessage", "Parola trebuie să conțină o literă mare. "));

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                        .param("username", "user10")
                        .param("password", "!Iuser101")
                        .param("email", "user10@user.com"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("errorMessage", "Parola nu trebuie să conțină username-ul. "));
    }


}
