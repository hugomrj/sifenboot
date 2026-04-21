package org.sifenboot.identity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        // Usamos BCrypt, que es el estándar de la industria
        // para proteger las claves en tu base de datos de San Lorenzo.
        return new BCryptPasswordEncoder();
    }
}