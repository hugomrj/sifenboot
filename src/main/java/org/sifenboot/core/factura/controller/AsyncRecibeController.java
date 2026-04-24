package org.sifenboot.core.factura.controller;

import org.sifenboot.core.factura.service.RecibirFacturaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.fasterxml.jackson.databind.JsonNode;


@RestController
//@RequestMapping("/factura/async/recibe")
@RequestMapping("/api/empresa/{emisor}")
@Tag(name = "Factura Async")
public class AsyncRecibeController {

    private final RecibirFacturaService recibirFacturaService;

    public AsyncRecibeController(RecibirFacturaService recibirFacturaService) {
        this.recibirFacturaService = recibirFacturaService;
    }

    @PostMapping("/factura/async/recibe")
    public ResponseEntity<?> asyncRecibe(
            @PathVariable String emisor,
            @RequestBody JsonNode json) {
        try {

            var result = recibirFacturaService.execute(emisor, json);
            return ResponseEntity.ok(result);
        
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Error interno: " + e.getMessage());
        }
    }




}
