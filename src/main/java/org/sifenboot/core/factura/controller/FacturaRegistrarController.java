package org.sifenboot.core.factura.controller;


import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sifenboot.core.factura.dto.request.FacturaProcesadaDTO;
import org.sifenboot.core.factura.service.FacturaRegistrarService;
import org.sifenboot.security.api.TokenService;
import org.sifenboot.errors.UnauthorizedException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/empresa/{emisor}")
@Tag(name = "Factura Registro")
public class FacturaRegistrarController {

    private final FacturaRegistrarService registrarFacturaService;
    private final TokenService tokenService;

    public FacturaRegistrarController(
            FacturaRegistrarService registrarFacturaService,
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

        FacturaProcesadaDTO resultado
                = registrarFacturaService.execute(emisor, request);
/*
        return ResponseEntity.accepted().build();
*/
        return ResponseEntity.ok(resultado);
    }
}
