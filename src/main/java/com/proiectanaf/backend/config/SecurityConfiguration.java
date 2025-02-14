package com.proiectanaf.backend.config;

import com.proiectanaf.backend.service.ServiciuUser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Clasa pentru configurarea securitatii aplicatiei folosind Spring Security
 * Constructorul primeste ca parametru un obiect de tip MyUserDetailService
 * Metoda securityFilterChain realizeaza configurarea securitatii aplicatiei
 * Metoda authenticationProvider realizeaza configurarea provider-ului de autentificare
 * Metoda bCryptPasswordEncoder realizeaza configurarea encoder-ului de parole
 * @autor Bolohan Marian-Cristian, 333AB
 * @version 7 Decembrie 2024
 */

@Configuration
public class SecurityConfiguration {

    private final ServiciuUser serviciuUser;

    public SecurityConfiguration(ServiciuUser serviciuUser) {
        this.serviciuUser = serviciuUser;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeRequests(authorize -> authorize
                        .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/","/login","/register", "/informatii", "/venituri", "/bunuri",
                                "/conturi_bancare", "istoric_taxe", "/plata_taxe", "/taxe", "/vizualizare_taxe",
                                "/vizualizare_tabele", "/vizualizare_persoane").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .loginPage("/login")  // Custom login page
                        .defaultSuccessUrl("/", true)  // redirctioneaza catre pagina principala dupa logare
                        .failureUrl("/login")  // redirectioneaza catre pagina de login in caz de esec
                        .permitAll())
                .logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout").invalidateHttpSession(true).clearAuthentication(true).permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement -> sessionManagement
                        .maximumSessions(-1)  // numaru nelimitat de sesiuni
                        .sessionRegistry(new SessionRegistryImpl())  // pentru a gestiona sesiunile
                )
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(serviciuUser);
        provider.setPasswordEncoder(bCryptPasswordEncoder());
        return provider;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

