package com.sifen.api.service.factura;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sifen.api.dto.Lote.response.LoteErrorResponse;
import com.sifen.api.dto.Lote.response.LoteOkResponse;
import com.sifen.api.integration.util.xml.FileXML;
import com.sifen.api.integration.builder.QrNodeBuilder;
import com.sifen.api.integration.util.xml.XmlUtils;
import com.sifen.api.repository.factura.FacturaRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Node;
import com.sifen.api.integration.util.xml.generator.DeXmlGenerator;
import com.sifen.api.integration.util.xml.sign.SifenXmlSigner;





@Service
public class RecibirFacturaService {

    private final DeXmlGenerator xmlGenerator;
    private final FacturaRepository facturaRepository;
    private final SifenXmlSigner xmlSifenSigner;
    private final QrNodeBuilder qrNodeBuilder;
    private final ObjectMapper objectMapper;


    @Autowired
    public RecibirFacturaService(
            FacturaRepository facturaRepository,
            DeXmlGenerator xmlGenerator,
            SifenXmlSigner xmlSifenSigner,
            QrNodeBuilder qrNodeBuilder,
            ObjectMapper objectMapper
    ) {
        this.facturaRepository = facturaRepository;
        this.xmlGenerator = xmlGenerator;
        this.xmlSifenSigner = xmlSifenSigner;
        this.qrNodeBuilder = qrNodeBuilder;
        this.objectMapper = objectMapper;
    }


    public Object execute(JsonNode facturaInput) throws Exception {


        System.out.println("== INICIO PROCESO FACTURA ==");

        // 1. Convertir JSON a XML
        String xmlGenerado = xmlGenerator.generar(facturaInput);

        // 2. Firmar
        Node nodoFirmado = xmlSifenSigner.signXml(xmlGenerado);

        // 3. Agregar QR
        Node nodoConQR = qrNodeBuilder.addQrNode(nodoFirmado);

        // 4. DOM → String
        String xmlFinal = FileXML.xmlToString(nodoConQR);

        // 5. Enviar al servidor
        JsonNode respuestaJson = facturaRepository.enviarFactura(xmlFinal);
        System.out.println(respuestaJson);


        JsonNode nodoRespuesta =
                respuestaJson
                        .path("env:Envelope")
                        .path("env:Body")
                        .path("ns2:rResEnviLoteDe");

        String codRes = nodoRespuesta.path("ns2:dCodRes").asText();


        // ✅ CASO OK
        if ("0300".equals(codRes)) {

            LoteOkResponse ok = new LoteOkResponse();
            ok.setDProtConsLote(nodoRespuesta.path("ns2:dProtConsLote").asText());
            ok.setDFecProc(nodoRespuesta.path("ns2:dFecProc").asText());
            ok.setDTpoProces(nodoRespuesta.path("ns2:dTpoProces").asInt());
            ok.setDMsgRes(nodoRespuesta.path("ns2:dMsgRes").asText());
            ok.setDCodRes(nodoRespuesta.path("ns2:dCodRes").asText());
            ok.setCdc( XmlUtils.obtenerCdcDesdeXml(xmlGenerado) );
            ok.setXmlFinal(xmlFinal);


            System.out.println("== FIN PROCESO FACTURA ==");

            return ok;
        }

        // ❌ CASO ERROR
        LoteErrorResponse error =
                objectMapper.treeToValue(nodoRespuesta, LoteErrorResponse.class);

        return error;
    }



}
