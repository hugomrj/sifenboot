package org.sifenboot.common.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Deshabilitado para facilitar peticiones AJAX/fetch
                .authorizeHttpRequests(auth -> auth
                        // --- 1. CAPA CORE (ERP / MÁQUINAS) ---
                        // Liberamos para que el Interceptor valide el API Key manualmente
                        .requestMatchers("/api/**").permitAll()

                        // --- 2. CAPA PANEL (PÁGINAS Y RECURSOS) ---
                        .requestMatchers("/css/**", "/js/**", "/login").permitAll()

                        // --- 3. CAPA APP (API PARA LA VISTA / SPA) ---
                        // El login debe ser público para poder entrar
                        .requestMatchers("/app/auth/login").permitAll()

                        // Todo lo demás de la App y el Dashboard requiere Sesión
                        .requestMatchers("/dashboard", "/app/**").authenticated()

                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login") // Redirección automática si no hay sesión
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/app/auth/logout")
                        .logoutSuccessHandler((req, res, auth) -> res.setStatus(204))
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }
}