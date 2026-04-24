package org.sifenboot.core.evento.controller;

import org.sifenboot.core.evento.dto.request.CancelarRequest;
import org.sifenboot.core.cancelar.service.EventoCancelarService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/{cod_emisor}")
@Tag(name = "Evento Cancelar")
public class EventoCancelarController {

    private final EventoCancelarService eventoCancelarService;

    public EventoCancelarController(EventoCancelarService eventoCancelarService) {
        this.eventoCancelarService = eventoCancelarService;
    }

    // Aquí defines la acción específica
    @PostMapping(value = "/evento/cancelar", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> eventoCancelar(
            @PathVariable("cod_emisor") String codEmisor,
            @RequestBody CancelarRequest request
    ) {
        var result = eventoCancelarService.execute(codEmisor, request);

        return ResponseEntity.ok(result);
    }
}


