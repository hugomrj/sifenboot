package org.sifenboot.core.factura.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.sifenboot.app.admin.documento.service.DocumentoCoreService;
import org.sifenboot.app.admin.emisor.model.Emisor;
import org.sifenboot.app.admin.emisor.service.EmisorService;
import org.sifenboot.core.factura.dto.request.FacturaProcesadaDTO;
import org.sifenboot.core.factura.service.mapping.JsonSifenMapping;
import org.sifenboot.core.factura.service.processor.*;
import org.sifenboot.core.integration.builder.QrNodeBuilder;
import org.sifenboot.core.integration.util.xml.FileXML;
import org.sifenboot.core.integration.util.xml.generator.DeXmlGenerator;
import org.sifenboot.core.integration.util.xml.sign.SifenXmlSigner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;


@Service
public class FacturaRegistrarService {

    public static final Logger log = LoggerFactory.getLogger(ContextoJsonProcessor.class);

    private final DeXmlGenerator xmlGenerator;

    private final SifenXmlSigner xmlSifenSigner;
    private final QrNodeBuilder qrNodeBuilder;
    private final DocumentoCoreService documentoCoreService;
    private final EmisorService emisorService;


    @Autowired
    public FacturaRegistrarService(
            DeXmlGenerator xmlGenerator,
            SifenXmlSigner xmlSifenSigner,
            QrNodeBuilder qrNodeBuilder,
            DocumentoCoreService documentoCoreService,
            EmisorService emisorService
    ) {

        this.xmlGenerator = xmlGenerator;
        this.xmlSifenSigner = xmlSifenSigner;
        this.qrNodeBuilder = qrNodeBuilder;
        this.documentoCoreService = documentoCoreService;
        this.emisorService = emisorService;
    }

    public FacturaProcesadaDTO execute(String emisorCod, JsonNode facturaInput) {

        System.out.println("\n========================================");
        System.out.println("== INICIO PROCESO FACTURA ASYNC ==");
        System.out.println("========================================");

        Emisor emisor = emisorService.findByCodEmisor(emisorCod);

        // =========================================================================
        // PASO 0: TRADUCCIÓN DE FORMATO ERP/FRONTEND A ESTRUCTURA ESTÁNDAR SIFEN
        // =========================================================================
        // Asegúrate de tener el logger definido en la clase donde ejecutas esto:
        // private static final Logger log = LoggerFactory.getLogger(TuClase.class);

        log.info("=== [ORIGINAL] ===\n{}", facturaInput.toPrettyString());




        JsonNode jsonProcessor = JsonSifenMapping.process(facturaInput);
        log.info("=== [INICIO] JSON Recibido despues de JsonSifenMapping ===\n{}", jsonProcessor.toPrettyString());

        // =========================================================================
        // PROCESADORES DE NEGOCIO (Operan ya sobre campos oficiales: iTipEmi, etc.)
        // =========================================================================

        // Inyecta datos del emisor en la raíz
        jsonProcessor = EmisorJsonProcessor.process(emisor, jsonProcessor);
        log.info("=== [POST] EmisorJsonProcessor ===\n{}", jsonProcessor.toPrettyString());

        // Resuelve monedas y conversiones si aplica
        jsonProcessor = ContextoJsonProcessor.process(jsonProcessor);
        log.info("=== [POST] ContextoJsonProcessor ===\n{}", jsonProcessor.toPrettyString());

        // Recorre el array de ítems y resuelve códigos de unidad de medida / empaques
        jsonProcessor = DetallesJsonProcessor.process(jsonProcessor);
        log.info("=== [POST] DetallesJsonProcessor ===\n{}", jsonProcessor.toPrettyString());

        // Cálculos de totales comerciales
        jsonProcessor = TotalesJsonProcessor.process(jsonProcessor);
        log.info("=== [POST] TotalesJsonProcessor ===\n{}", jsonProcessor.toPrettyString());

        // Cálculos de IVA, bases gravadas y exclusiones por iTImp
        jsonProcessor = ImpuestoJsonProcessor.process(jsonProcessor);
        log.info("=== [FIN] ImpuestoJsonProcessor (Resultado Final) ===\n{}", jsonProcessor.toPrettyString());






    System.out.println(jsonProcessor.toPrettyString());

        // =========================
        // GENERAR XML
        // =========================

        System.out.println("\n[2/6] Generando XML...");

        String xmlGenerado = null;
        try {
            xmlGenerado = xmlGenerator.generar(jsonProcessor);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println("✔ XML generado");
        System.out.println("Tamaño XML: " + xmlGenerado.length());
        System.out.println(xmlGenerado);

        // =========================
        // FIRMAR XML
        // =========================

        System.out.println("\n[3/6] Firmando XML...");

        Node nodoFirmado = xmlSifenSigner.signXml(emisorCod, xmlGenerado);

        System.out.println("✔ XML firmado");


        // =========================
        // AGREGAR QR
        // =========================

        System.out.println("\n[4/6] Agregando QR...");

        Node nodoConQR = qrNodeBuilder.addQrNode(emisorCod, nodoFirmado);

        System.out.println("✔ QR agregado");

        // =========================
        // XML FINAL
        // =========================

        System.out.println("\n[5/6] Generando XML final...");

        String xmlDE = FileXML.xmlToString(nodoConQR);

        System.out.println("✔ XML final generado");
        //System.out.println("Tamaño final XML: " + xmlDE.length());

        // =========================
        // REGISTRAR DOCUMENTO
        // =========================

        System.out.println("\n[6/6] Registrando documento...");

        var documento = documentoCoreService.registrarDocumentoFirmado(
                emisorCod,
                facturaInput,
                xmlDE
        );

        System.out.println("✔ Documento registrado");

        // =========================
        // FIN
        // =========================

        System.out.println("\n========================================");
        System.out.println("== FIN PROCESO FACTURA ASYNC ==");
        System.out.println("========================================\n");

        // 6. Retornar el DTO con los datos del proceso
        return new FacturaProcesadaDTO(
                emisorCod,
                documento.getCdc(),
                xmlDE,
                documento.getEstado().getCodigo()
        );


    }



}