package org.sifenboot.core.lote.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sifenboot.core.integration.soap.client.LoteClient;
import org.sifenboot.core.integration.util.message.SoapBodyExtractor;
import org.json.JSONObject;
import org.json.XML;
import org.sifenboot.security.certificado.model.Certificado;
import org.springframework.stereotype.Repository;

import java.net.http.HttpResponse;


@Repository
public class LoteRepository {

    private final LoteClient loteClient;
    private final ObjectMapper mapper = new ObjectMapper();

    public LoteRepository(LoteClient loteClient) {
        this.loteClient = loteClient;
    }

    public JsonNode consultarLote(String lote, Certificado certificado) {

        try {
            HttpResponse<String> httpResponse = loteClient.consultaLote(lote, certificado );
            int statusCode = httpResponse.statusCode();

            // Solo error real si es 500+
            if (statusCode >= 500) {
                return mapper.createObjectNode()
                        .put("error", "HTTP error: " + statusCode)
                        .put("statusCode", statusCode);
            }

            String xml = httpResponse.body();

            // Extraer solo el Body del SOAP
            String xmlLimpio = SoapBodyExtractor.extractBody(xml);

            // XML → JSON
            JSONObject jsonObj = XML.toJSONObject(xmlLimpio);
            JsonNode root = mapper.readTree(jsonObj.toString());

            // Nodo real según el XSD de consulta lote
            JsonNode rRes = root
                    .path("Envelope")
                    .path("Body")
                    .path("rResEnviConsLoteDe");

            return rRes;

        } catch (Exception e) {
            return mapper.createObjectNode()
                    .put("error", "Error consultando lote: " + e.getMessage());
        }
    }
}
