package org.sifenboot.shell.service;

import org.sifenboot.shell.dto.LoginRequest;
import org.sifenboot.shell.dto.UserDto;
import org.sifenboot.shell.model.User;
import org.sifenboot.shell.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

        // 2. Validar contraseña
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
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