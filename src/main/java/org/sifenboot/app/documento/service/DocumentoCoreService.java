package org.sifenboot.app.documento.service;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.transaction.Transactional;
import org.sifenboot.app.documento.model.Documento;
import org.sifenboot.app.documento.model.EstadoDocumento;
import org.sifenboot.app.documento.repository.DocumentoRepository;

import org.sifenboot.app.documento.repository.EstadoDocumentoRepository;
import org.sifenboot.core.integration.builder.DeXmlBuilder;
import org.sifenboot.setup.db.DbUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class DocumentoCoreService {

    private final DocumentoRepository documentoRepository;
    private final EstadoDocumentoRepository estadoDocumentoRepository;
    private final DbUtils db;
    private final DeXmlBuilder deXmlBuilder;


    public DocumentoCoreService(DocumentoRepository documentoRepository,
                                DbUtils db,
                                EstadoDocumentoRepository estadoDocumentoRepository,
                                DeXmlBuilder deXmlBuilder) {
        this.documentoRepository = documentoRepository;
        this.db = db;
        this.estadoDocumentoRepository = estadoDocumentoRepository;
        this.deXmlBuilder = deXmlBuilder;
    }


    @Transactional
    public Documento registrarDocumentoFirmado(String codEmisor,
                                               JsonNode facturaJson, String xmlFirmado) {

        // 1. CDC primero (independiente de schema)
        String cdc = deXmlBuilder.getCDC(xmlFirmado);
        if (cdc == null || cdc.isBlank() || "error".equals(cdc)) {
            throw new RuntimeException("No se pudo obtener CDC del XML firmado");
        }

        // 2. catálogo (public)
        db.setSchema("public");

        EstadoDocumento estado = estadoDocumentoRepository
                .findByCodigo("RECIBIDO")
                .orElseThrow(() -> new RuntimeException("Estado RECIBIDO no existe"));

        // 3. schema del emisor
        db.setSchema(codEmisor);

        Documento documento = new Documento();

        documento.setTipoDocumento(facturaJson.path("iTiDE").asInt());
        documento.setEstablecimiento(facturaJson.path("dEst").asText());
        documento.setPuntoExpedicion(facturaJson.path("dPunExp").asText());
        documento.setNumeroDocumento(facturaJson.path("dNumDoc").asText());
        documento.setCdc(cdc);

        documento.setMontoTotal(
                new BigDecimal(facturaJson.path("dTotGralOpe").asText("0"))
        );

        documento.setNombreReceptor(facturaJson.path("dNomRec").asText());

        String ruc = facturaJson.path("dRucRec").asText(null);
        String dv  = facturaJson.path("dDVRec").asText(null);

        if (ruc != null && !ruc.isBlank()) {
            documento.setRucReceptor(
                    (dv != null && !dv.isBlank()) ? ruc + "-" + dv : ruc
            );
        }

        documento.setXmlEnviado(xmlFirmado);
        documento.setJsonData(facturaJson.toString());

        LocalDateTime ahora = LocalDateTime.now();
        documento.setFechaCreacion(ahora);
        documento.setFechaEmision(ahora);

        documento.setEstado(estado);

        return documentoRepository.save(documento);
    }


}