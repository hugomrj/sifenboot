package org.sifenboot.core.factura.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

@Service
public class RegistrarFacturaService {

    public void execute(String emisorCod, JsonNode facturaInput) {

        // TODO: enviar a cola / hilo async / procesamiento diferido

        System.out.println("== INICIO PROCESO FACTURA ASYNC ==");

        // 1. Recibir JSON
        // 2. Encolar o procesar en segundo plano
        // 3. No bloquear respuesta

        System.out.println("== FIN PROCESO FACTURA ASYNC ==");
    }

}