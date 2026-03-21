package com.sifen.api.service.lote;

import com.fasterxml.jackson.databind.JsonNode;

import com.sifen.api.dto.Lote.response.ConsultaLoteResponse;
import com.sifen.api.repository.lote.LoteRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConsultaLoteService {

    private final LoteRepository loteRepository;

    public ConsultaLoteService(LoteRepository loteRepository) {
        this.loteRepository = loteRepository;
    }

    public ConsultaLoteResponse consultar(String lote) {

        JsonNode res = loteRepository.consultarLote(lote);

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
