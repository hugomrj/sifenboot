package org.sifenboot.core.lote.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.sifenboot.app.admin.emisor.model.Emisor;
import org.sifenboot.core.lote.repository.LoteEnvioRepository;
import org.sifenboot.security.certificado.model.Certificado;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoteEnvioService {

    private final LoteEnvioRepository repository;

    public LoteEnvioService( LoteEnvioRepository repository ) {
        this.repository = repository;
    }

    public JsonNode procesarPendientes(
            Emisor emisor,
            Certificado certificado
    ) {

        /*
         * SIMULACIÓN:
         * después esto vendrá desde DB
         */

        List<String> xmlsPendientes = new ArrayList<>();

        xmlsPendientes.add("""
                <DE>
                    <Id>1</Id>
                </DE>
                """);

        xmlsPendientes.add("""
                <DE>
                    <Id>2</Id>
                </DE>
                """);

        xmlsPendientes.add("""
                <DE>
                    <Id>3</Id>
                </DE>
                """);

        /*
         * Cortar máximo 50
         */

        List<String> lote = xmlsPendientes.stream()
                .limit(50)
                .toList();

        /*
         * Enviar lote
         */

        JsonNode respuesta = repository.enviarLote(
                lote,
                certificado
        );

        System.out.println(respuesta.toPrettyString());

        return respuesta;
    }
}