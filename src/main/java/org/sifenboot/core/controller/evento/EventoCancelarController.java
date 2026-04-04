package org.sifenboot.core.controller.evento;

import org.sifenboot.core.dto.evento.request.CancelarRequest;
import org.sifenboot.core.service.evento.EventoCancelarService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/evento/cancelar")
@Tag(name = "Evento Cancelar")
public class EventoCancelarController {

    private final EventoCancelarService eventoCancelarService;

    public EventoCancelarController(EventoCancelarService eventoCancelarService) {
        this.eventoCancelarService = eventoCancelarService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> eventoCancelar(
            @RequestBody CancelarRequest request
    ) {
        var result = eventoCancelarService.execute(request);
        return ResponseEntity.ok(result);
    }

}
