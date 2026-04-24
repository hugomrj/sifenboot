package org.sifenboot.core.factura.controller;

import org.sifenboot.core.factura.dto.request.ConsultaCdcRequest;
import org.sifenboot.core.factura.dto.response.ConsultaCdcResponse;
import org.sifenboot.core.factura.service.ConsultarCdcService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/empresa/{emisor}")
@Tag(name = "Consulta CDC")
public class ConsultaCdcController {

    private final ConsultarCdcService consultarCdcService;

    public ConsultaCdcController(ConsultarCdcService consultarCdcService) {
        this.consultarCdcService = consultarCdcService;
    }

    @PostMapping("/consulta/cdc")
    public ResponseEntity<ConsultaCdcResponse> consultarCdc(
            @PathVariable String emisor,
            @Valid @RequestBody ConsultaCdcRequest request) {

        ConsultaCdcResponse response =
                consultarCdcService.consultar( emisor, request.getCdc());

        return ResponseEntity.ok(response);
    }

}