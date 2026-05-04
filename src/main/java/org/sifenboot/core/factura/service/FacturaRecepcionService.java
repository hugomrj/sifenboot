package org.sifenboot.core.factura.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

@Service
public class FacturaRecepcionService {

    public void execute(String emisorCod, JsonNode facturaInput) {

        // TODO: enviar a cola / hilo async / procesamiento diferido
        /*
        *   se recibe factura json,
        *   se carga con los datos que estan cargados en emisor
        *   se completa el json com ambas partes
        *   se ve para transormar el json con el json con nombre de la sifen
        * */


        System.out.println("== INICIO PROCESO FACTURA ASYNC ==");

        // 1. Recibir JSON
        // 2. Encolar o procesar en segundo plano
        // 3. No bloquear respuesta

        System.out.println("== FIN PROCESO FACTURA ASYNC ==");
    }

}