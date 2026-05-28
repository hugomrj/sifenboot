package org.sifenboot.core.ruc.controller;

import org.sifenboot.core.ruc.dto.request.ConsultaRucRequest;
import org.sifenboot.core.ruc.service.ConsultarRucService;
import jakarta.validation.Valid;
import org.sifenboot.errors.UnauthorizedException;
import org.sifenboot.security.api.TokenService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/empresa/{emisor}")
public class ConsultaRucController {

    private final ConsultarRucService consultarRucService;
    private final TokenService tokenService;

    public ConsultaRucController(ConsultarRucService consultarRucService,
                                 TokenService tokenService ) {
        this.consultarRucService = consultarRucService;
        this.tokenService = tokenService;
    }

    @PostMapping("/consulta/ruc")
    public ResponseEntity<?> consultarRuc(
            @PathVariable String emisor,
            @RequestHeader(value = "X-API-KEY", defaultValue = "") String token,
            @Valid @RequestBody ConsultaRucRequest request) {

        if (!tokenService.esTokenValido(emisor, token)) {
            throw new UnauthorizedException("Token inválido o no autorizado para el emisor: "
                    + emisor);
        }

        var result = consultarRucService.consultar(emisor, request.getRuc());
        return ResponseEntity.ok(result);
    }
}