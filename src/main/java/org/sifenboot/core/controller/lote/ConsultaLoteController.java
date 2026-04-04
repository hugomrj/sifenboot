package org.sifenboot.core.controller.lote;

import org.sifenboot.core.dto.Lote.request.ConsultaLoteRequest;
import org.sifenboot.core.service.lote.ConsultaLoteService;
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
