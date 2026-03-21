package com.sifen.api.controller.lote;

import com.sifen.api.dto.Lote.request.ConsultaLoteRequest;
import com.sifen.api.service.lote.ConsultaLoteService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consulta/lote")
@Tag(name = "Consulta Lote")
public class ConsultaLoteController {

    private final ConsultaLoteService consultaLoteService;

    public ConsultaLoteController(ConsultaLoteService consultaLoteService) {
        this.consultaLoteService = consultaLoteService;
    }


    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> consultarLote(
            @Valid @RequestBody ConsultaLoteRequest request
    ) {
        var result = consultaLoteService.consultar(request.getLote());
        return ResponseEntity.ok(result);
    }
}
