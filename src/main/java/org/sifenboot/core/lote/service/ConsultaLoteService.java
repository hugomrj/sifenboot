package org.sifenboot.core.lote.service;

import com.fasterxml.jackson.databind.JsonNode;

import org.sifenboot.core.lote.dto.response.ConsultaLoteResponse;
import org.sifenboot.core.lote.repository.LoteRepository;
import org.sifenboot.security.certificado.model.Certificado;
import org.sifenboot.security.certificado.service.CertificadoService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConsultaLoteService {

    private final CertificadoService certificadoService;
    private final LoteRepository repository;

    public ConsultaLoteService(LoteRepository repository, CertificadoService certificadoService) {
        this.repository = repository;
        this.certificadoService = certificadoService;
    }


    public ConsultaLoteResponse consultar(String emisorCod, String lote) {

        // Obtener el certificado
        Certificado certificado = certificadoService.getActiveCertificateByEmisorCode(emisorCod);

        JsonNode res = repository.consultarLote(lote, certificado);

        String codigoLote = res.path("dCodResLot").asText();
        String mensajeLote = res.path("dMsgResLot").asText();

        List<ConsultaLoteResponse.Resultado> resultados = new ArrayList<>();

        JsonNode loteNode = res.path("gResProcLote");

        if (!loteNode.isMissingNode()) {

            String cdc = loteNode.path("id").asText();
            String estado = loteNode.path("dEstRes").asText();

            JsonNode mensajes = loteNode.path("gResProc");

            if ("Rechazado".equalsIgnoreCase(estado)) {

                resultados.add(
                        new ConsultaLoteResponse.Resultado(
                                cdc,
                                estado,
                                mensajes.path("dCodRes").asInt(),
                                mensajes.path("dMsgRes").asText()
                        )
                );

            } else {
                resultados.add(
                        new ConsultaLoteResponse.Resultado(
                                cdc,
                                estado,
                                null,
                                null
                        )
                );
            }
        }

        return new ConsultaLoteResponse(
                codigoLote,
                mensajeLote,
                resultados
        );
    }



}
