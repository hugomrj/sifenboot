package com.sifen.api.controller.factura;


import com.sifen.api.dto.factura.request.ConsultaCdcRequest;
import com.sifen.api.dto.factura.response.ConsultaCdcResponse;
import com.sifen.api.service.factura.ConsultarCdcService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/consulta/cdc")
@Tag(name = "Consulta CDC")
public class ConsultaCdcController {

    private final ConsultarCdcService consultarCdcService;

    public ConsultaCdcController(ConsultarCdcService consultarCdcService) {
        this.consultarCdcService = consultarCdcService;
    }

    @PostMapping(
            consumes = "application/json",
            produces = "application/json"
    )
    public ResponseEntity<ConsultaCdcResponse> consultarCdc(
            @Valid @RequestBody ConsultaCdcRequest request
    ) {
        ConsultaCdcResponse response =
                consultarCdcService.consultar(request.getCdc());

        return ResponseEntity.ok(response);
    }

}