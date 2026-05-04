package org.sifenboot.core.factura.controller;


import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sifenboot.core.factura.service.FacturaRecepcionService;
import org.sifenboot.core.security.service.TokenService;
import org.sifenboot.errors.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/empresa/{emisor}")
@Tag(name = "Factura Registro")
public class FacturaRecepcionController {

    private final FacturaRecepcionService registrarFacturaService;
    private final TokenService tokenService;

    public FacturaRecepcionController(
            FacturaRecepcionService registrarFacturaService,
            TokenService tokenService
    ) {
        this.registrarFacturaService = registrarFacturaService;
        this.tokenService = tokenService;
    }

    @PostMapping("/factura/registrar")
    public ResponseEntity<?> registrarFactura(
            @PathVariable String emisor,
            @RequestHeader(value = "X-API-KEY", defaultValue = "") String token,
            @RequestBody JsonNode request
    ) {

        if (!tokenService.esTokenValido(emisor, token)) {
            throw new UnauthorizedException("Token inválido o no autorizado para el emisor: "
                    + emisor);
        }

        registrarFacturaService.execute(emisor, request);
/*
        return ResponseEntity.accepted().build();
*/
        return ResponseEntity.ok(Map.of("status", "success",
                "message", "Recibido en modo prueba"));
    }
}
