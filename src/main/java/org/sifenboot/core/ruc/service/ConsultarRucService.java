package org.sifenboot.core.ruc.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.sifenboot.core.ruc.dto.response.RucExisteResponse;
import org.sifenboot.core.ruc.dto.response.RucNoExisteResponse;
import org.sifenboot.core.ruc.repository.RucRepository;
import org.sifenboot.security.certificado.model.Certificado;
import org.sifenboot.security.certificado.service.CertificadoService;
import org.springframework.stereotype.Service;


@Service
public class ConsultarRucService {

    private final RucRepository repository;
    private final CertificadoService certificadoService;

    public ConsultarRucService(RucRepository rucRepository, CertificadoService certificadoService) {
        this.repository = rucRepository;
        this.certificadoService = certificadoService;
    }

    // Ahora recibimos el código del emisor (ej: 'wasabi')
    public Object consultar(String emisorCod, String ruc) {

        if (ruc == null || ruc.isBlank()) {
            throw new IllegalArgumentException("El RUC no puede estar vacío");
        }

        // Obtener el certificado
        Certificado certificado = certificadoService.obtenerPorCodigoEmisor(emisorCod);

        // 2. Le pasamos el certificado al repositorio para que firme la petición
        JsonNode result = repository.buscarPorRuc(ruc, certificado);

        String codigo = result.path("codigoRespuesta").asText();

        // --- RUC NO EXISTE (0500) ---
        if ("0500".equals(codigo)) {
            return new RucNoExisteResponse(
                    codigo,
                    result.path("mensajeRespuesta").asText("RUC no existe"),
                    result.path("statusCode").asInt(200),
                    result.path("mensaje").asText("RUC no existe"),
                    ruc
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

        throw new IllegalStateException("Código de respuesta no reconocido: " + codigo);
    }
}