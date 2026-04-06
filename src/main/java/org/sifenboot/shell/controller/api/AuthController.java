package org.sifenboot.shell.controller.api;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.sifenboot.shell.dto.LoginRequest;
import org.sifenboot.shell.dto.UserDto;
import org.sifenboot.shell.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections; // <--- NO OLVIDAR ESTE

@RestController
@RequestMapping("/app/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@Valid @RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        // 1. Lógica de negocio (validación en DB, RUC, etc.)
        UserDto response = authService.login(request);

        // 2. Registramos la autenticación en el hilo actual de ejecución
        UsernamePasswordAuthenticationToken authReq =
                new UsernamePasswordAuthenticationToken(response.getUsername(), null, Collections.emptyList());

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(authReq);

        // 3. Persistimos el contexto en la Sesión de Servlet (para que el F5 funcione)
        // true: crea la sesión si no existe
        HttpSession session = httpRequest.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, sc);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        // 1. Obtenemos la sesión actual (si existe) y la destruimos
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate(); // Mata el JSESSIONID en el servidor
        }

        // 2. Limpiamos el contexto de seguridad del hilo actual
        SecurityContextHolder.clearContext();

        return ResponseEntity.noContent().build();
    }
}