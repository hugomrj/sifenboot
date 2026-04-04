package org.sifenboot.core.service.ruc;

import com.fasterxml.jackson.databind.JsonNode;
import org.sifenboot.core.dto.ruc.response.RucExisteResponse;
import org.sifenboot.core.dto.ruc.response.RucNoExisteResponse;
import org.sifenboot.core.repository.ruc.RucRepository;
import org.springframework.stereotype.Service;

@Service
public class ConsultarRucService {

    private final RucRepository rucRepository;

    public ConsultarRucService(RucRepository rucRepository) {
        this.rucRepository = rucRepository;
    }

    public Object consultar(String ruc) {

        if (ruc == null || ruc.isBlank()) {
            throw new IllegalArgumentException("El RUC no puede estar vacío");
        }

        JsonNode result = rucRepository.buscarPorRuc(ruc);

        String codigo = result.path("codigoRespuesta").asText();

        // --- RUC NO EXISTE (0500) ---
        if ("0500".equals(codigo)) {
            return new RucNoExisteResponse(
                    codigo,
                    result.path("mensajeRespuesta").asText("RUC no existe"),
                    result.path("statusCode").asInt(200),
                    result.path("mensaje").asText("RUC no existe"),
                    ruc    // ← agregar esto
            );
        }


        
        // --- RUC EXISTE (0502) ---
        if ("0502".equals(codigo)) {
            JsonNode datos = result.path("datosRUC");

            return new RucExisteResponse(
                    codigo,
                    result.path("mensajeRespuesta").asText("RUC encontrado"),
                    result.path("statusCode").asInt(200),
                    new RucExisteResponse.DatosRuc(
                            datos.path("estado").asText(),
                            datos.path("nombre").asText(),
                            datos.path("RUCdv").asText(),
                            datos.path("numeroRUC").asLong(),
                            datos.path("facturaElectronica").asText(),
                            datos.path("codigoEstado").asText()
                    )
            );
        }

        // --- Código inesperado ---
        throw new IllegalStateException("Código de respuesta no reconocido: " + codigo);
        //throw new CodeSifenRUCNofFoundException("Código de respuesta no reconocido: " + codigo);
    }
}
