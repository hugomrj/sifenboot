package org.sifenboot.core.lote.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.sifenboot.app.admin.documento.model.Documento;
import org.sifenboot.app.admin.documento.model.EstadoDocumento;
import org.sifenboot.app.admin.documento.repository.DocumentoRepository;
import org.sifenboot.app.admin.emisor.model.Emisor;
import org.sifenboot.core.lote.repository.LoteEnvioRepository;
import org.sifenboot.security.certificado.model.Certificado;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoteEnvioService {

    private final LoteEnvioRepository repository;
    private final DocumentoRepository documentoRepository;

    public LoteEnvioService(
            LoteEnvioRepository repository,
            DocumentoRepository documentoRepository
    ) {
        this.repository = repository;
        this.documentoRepository = documentoRepository;
    }

    public JsonNode procesarPendientes(
            Emisor emisor,
            Certificado certificado
    ) {

        /*
         * Buscar documentos recibidos
         */

        List<Documento> documentosPendientes =
                documentoRepository
                        .findTop50ByEstadoIdOrderByFechaCreacionAsc(
                                EstadoDocumento.RECIBIDO
                        );

        /*
         * Extraer XMLs
         */

        List<String> lote = documentosPendientes.stream()
                .map(Documento::getXmlEnviado)
                .filter(xml -> xml != null && !xml.isBlank())
                .toList();

        /*
         * No hay documentos
         */

        if (lote.isEmpty()) {
            return null;
        }

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