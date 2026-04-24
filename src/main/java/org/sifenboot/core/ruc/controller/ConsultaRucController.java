package org.sifenboot.core.ruc.controller;

import org.sifenboot.core.ruc.dto.request.ConsultaRucRequest;
import org.sifenboot.core.ruc.service.ConsultarRucService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/empresa/{emisor}")
public class ConsultaRucController {

    private final ConsultarRucService consultarRucService;

    public ConsultaRucController(ConsultarRucService consultarRucService) {
        this.consultarRucService = consultarRucService;
    }

    @PostMapping("/consulta/ruc")
    public ResponseEntity<?> consultarRuc(
            @PathVariable String emisor,
            @Valid @RequestBody ConsultaRucRequest request) {

        var result = consultarRucService.consultar(emisor, request.getRuc());
        return ResponseEntity.ok(result);
    }
}