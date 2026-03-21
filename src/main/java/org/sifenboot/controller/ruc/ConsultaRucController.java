package org.sifenboot.controller.ruc;

import org.sifenboot.dto.ruc.request.ConsultaRucRequest;
import org.sifenboot.service.ruc.ConsultarRucService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/consulta/ruc")
@Tag(name = "Consulta Ruc")
public class ConsultaRucController {

    private final ConsultarRucService consultarRucService;
    public ConsultaRucController(ConsultarRucService consultarRucService) {
        this.consultarRucService = consultarRucService;
    }


    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> consultarRuc(@Valid @RequestBody ConsultaRucRequest request) {

        var result = consultarRucService.consultar(request.getRuc());
        return ResponseEntity.ok(result);
    }
}
