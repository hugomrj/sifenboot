package org.sifenboot.security.config.identity;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                // Configuración para manejar la sesión expirada
                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint(htmxAuthenticationEntryPoint())
                )
                .authorizeHttpRequests(auth -> auth
                        // --- SWAGGER ---
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        // --- 1. CAPA CORE (ERP / MÁQUINAS) ---
                        .requestMatchers("/api/**").permitAll()
                        // --- 2. CAPA PANEL ---
                        .requestMatchers("/css/**", "/js/**", "/login").permitAll()
                        // --- 3. CAPA APP ---
                        .requestMatchers("/app/auth/login").permitAll()
                        .requestMatchers("/dashboard", "/app/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
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

    @Bean
    public AuthenticationEntryPoint htmxAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            if ("true".equals(request.getHeader("HX-Request"))) {
                // Si es petición HTMX, responde con el header para redirección forzada
                response.setHeader("HX-Redirect", "/login");
                response.setStatus(200);
            } else {
                // Si es petición normal, redirecciona al login
                response.sendRedirect("/login");
            }
        };
    }
}