package org.sifenboot.controller.factura;

import org.sifenboot.service.factura.RecibirFacturaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.fasterxml.jackson.databind.JsonNode;


@RestController
@RequestMapping("/factura/async/recibe")
@Tag(name = "Factura Async")
public class AsyncRecibeController {

    private final RecibirFacturaService recibirFacturaService;

    public AsyncRecibeController(RecibirFacturaService recibirFacturaService) {
        this.recibirFacturaService = recibirFacturaService;
    }

    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> asyncRecibe(@RequestBody JsonNode json) {
        try {

            var result = recibirFacturaService.execute(json);
            return ResponseEntity.ok(result);
        
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error interno: " + e.getMessage());
        }
    }
}
