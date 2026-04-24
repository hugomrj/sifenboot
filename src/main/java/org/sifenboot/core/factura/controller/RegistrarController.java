package org.sifenboot.core.factura.controller;


import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sifenboot.core.factura.service.RegistrarFacturaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/empresa/{emisor}")
@Tag(name = "Factura Registro")
public class RegistrarController {

    private final RegistrarFacturaService registrarFacturaService;

    public RegistrarController(RegistrarFacturaService registrarFacturaService) {
        this.registrarFacturaService = registrarFacturaService;
    }

    @PostMapping("/factura/registrar")
    public ResponseEntity<?> registrarFactura(
            @PathVariable String emisor,
            @RequestBody JsonNode request
    ) {
        registrarFacturaService.execute(emisor, request);
        return ResponseEntity.accepted().build();
    }
}
