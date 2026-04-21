package org.sifenboot.core.controller.ruc;

import org.sifenboot.core.dto.ruc.request.ConsultaRucRequest;
import org.sifenboot.core.service.ruc.ConsultarRucService;
import io.swagger.v3.oas.annotations.tags.Tag;
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

        var result = consultarRucService.consultar(request.getRuc());
        return ResponseEntity.ok(result);
    }
}