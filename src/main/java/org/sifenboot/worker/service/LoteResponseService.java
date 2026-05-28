package org.sifenboot.worker.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoteResponseService {

    public void procesarRespuesta(
            JsonNode respuesta,
            List<String> documentos
    ) {

        System.out.println(
                "[Respuesta] Procesando respuesta SIFEN"
        );

        System.out.println(respuesta);
    }
}