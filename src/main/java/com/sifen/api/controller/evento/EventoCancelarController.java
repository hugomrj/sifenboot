package com.sifen.api.controller.evento;

import com.sifen.api.dto.evento.request.CancelarRequest;
import com.sifen.api.service.evento.EventoCancelarService;
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
