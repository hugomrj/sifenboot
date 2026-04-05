package org.sifenboot.panel.service;

import org.sifenboot.panel.dto.LoginRequest;
import org.sifenboot.panel.dto.UserDto;
import org.sifenboot.panel.model.User;
import org.sifenboot.panel.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

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
            // Al lanzar esto, Spring Boot busca si hay un manejador.
            // Si no querés tocar el GlobalExceptionHandler, podés usar:
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        // 3. Éxito
        UserDto dto = new UserDto();
        dto.setUsername(user.getUsername());
        dto.setAccessToken("TOKEN_TEMPORAL_SIFENBOOT");
        return dto;
    }


}