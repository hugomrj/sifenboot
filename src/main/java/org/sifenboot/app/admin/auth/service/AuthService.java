package org.sifenboot.app.admin.auth.service;

import org.sifenboot.errors.UnauthorizedException;
import org.sifenboot.app.admin.auth.dto.LoginRequest;
import org.sifenboot.app.admin.auth.dto.UserDto;
import org.sifenboot.app.admin.auth.model.User;
import org.sifenboot.app.admin.auth.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// --- NUEVOS IMPORTS PARA LA SESIÓN ---
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.servlet.http.HttpSession;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public UserDto login(LoginRequest request) {
        // 1. Buscar usuario
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new UnauthorizedException("Credenciales inválidas")
                );



        // 2. Validar contraseña
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Credenciales inválidas");
        }



        // 3. CAPTURAR E IMPRIMIR JSESSIONID
        // Esto obtiene la sesión que Spring Security ya creó o crea una nueva si no existe
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        String jSessionId = session.getId();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("JSESSIONID: " + jSessionId);
        System.out.println("=".repeat(60) + "\n");

        // 4. Éxito
        UserDto dto = new UserDto();
        dto.setUsername(user.getUsername());
        // Aprovechamos y le pasamos el ID real al DTO por si lo necesitás en el front
        dto.setAccessToken(jSessionId);

        return dto;
    }
}