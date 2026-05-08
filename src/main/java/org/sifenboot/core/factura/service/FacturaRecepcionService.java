package org.sifenboot.core.factura.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sifenboot.app.documento.service.DocumentoCoreService;
import org.sifenboot.core.factura.repository.FacturaRepository;
import org.sifenboot.core.integration.builder.QrNodeBuilder;
import org.sifenboot.core.integration.util.xml.FileXML;
import org.sifenboot.core.integration.util.xml.generator.DeXmlGenerator;
import org.sifenboot.core.integration.util.xml.sign.SifenXmlSigner;
import org.sifenboot.security.certificado.service.CertificadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;


@Service
public class FacturaRecepcionService {

    private final DeXmlGenerator xmlGenerator;

    private final SifenXmlSigner xmlSifenSigner;
    private final QrNodeBuilder qrNodeBuilder;
    private final DocumentoCoreService documentoCoreService;


    @Autowired
    public FacturaRecepcionService(
            DeXmlGenerator xmlGenerator,
            SifenXmlSigner xmlSifenSigner,
            QrNodeBuilder qrNodeBuilder,
            DocumentoCoreService documentoCoreService
    ) {

        this.xmlGenerator = xmlGenerator;
        this.xmlSifenSigner = xmlSifenSigner;
        this.qrNodeBuilder = qrNodeBuilder;
        this.documentoCoreService = documentoCoreService;
    }

    public void execute(String emisorCod, JsonNode facturaInput) {



        System.out.println("== INICIO PROCESO FACTURA ASYNC ==");

        /*
         * 1. Recibir JSON
         * 2. Completar datos del emisor
         * 3. Transformar formato interno → formato SIFEN
         * 4. Generar XML
         * 5. Firmar XML
         * 6. Agregar QR
         */

        // 1. Convertir JSON a XML
        String xmlGenerado = null;
        try {
            xmlGenerado = xmlGenerator.generar(facturaInput);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 2. Firmar XML
        Node nodoFirmado = xmlSifenSigner.signXml(emisorCod, xmlGenerado);

        // 3. Agregar QR
        Node nodoConQR = qrNodeBuilder.addQrNode(nodoFirmado);

        // 4. DOM → String
        String xmlFinal = FileXML.xmlToString(nodoConQR);

        documentoCoreService.registrarDocumentoFirmado(
                emisorCod, facturaInput, xmlFinal);


        System.out.println(xmlFinal);

        // TODO:
        // guardar XML
        // enviar a SIFEN
        // persistir estado
        // manejar cola async

        System.out.println("== FIN PROCESO FACTURA ASYNC ==");


    }
}