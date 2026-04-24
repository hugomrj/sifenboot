package org.sifenboot.core.ruc.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.json.XML;

import org.sifenboot.core.sifen.util.SifenDvCalculator;
import org.sifenboot.core.integration.soap.client.RucClient;
import org.sifenboot.core.integration.util.message.SoapBodyExtractor;

import org.sifenboot.security.certificado.model.Certificado;
import org.springframework.stereotype.Repository;

import java.net.http.HttpResponse;

@Repository
public class RucRepository {

    private final RucClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    public RucRepository(RucClient client) {
        this.client = client;
    }


        public JsonNode buscarPorRuc(String ruc, Certificado certificado) {
            try {
                HttpResponse<String> httpResponse = client.consultaRUC(ruc, certificado);

            int statusCode = httpResponse.statusCode();

            // Solo considerar error cuando es 500+
            if (statusCode >= 500) {
                return mapper.createObjectNode()
                        .put("error", "HTTP error: " + statusCode)
                        .put("statusCode", statusCode);
            }

            String xml = httpResponse.body();

            String xmlLimpio = SoapBodyExtractor.extractBody(xml);

            JSONObject jsonObj = XML.toJSONObject(xmlLimpio);
            JsonNode root = mapper.readTree(jsonObj.toString());

            JsonNode rRes = root
                    .path("Envelope")
                    .path("Body")
                    .path("rResEnviConsRUC");

            String dCodRes = rRes.path("dCodRes").asText();
            String dMsgRes = rRes.path("dMsgRes").asText();

            var response = mapper.createObjectNode();
            response.put("codigoRespuesta", dCodRes);
            response.put("mensajeRespuesta", dMsgRes);
            response.put("statusCode", statusCode);

            // Caso RUC encontrado
            if ("0502".equals(dCodRes) && rRes.has("xContRUC")) {

                JsonNode xContRUC = rRes.path("xContRUC");
                String dRUCCons = xContRUC.path("dRUCCons").asText();
                String RUCdv = SifenDvCalculator.generateDvRuc(dRUCCons);

                var datosRuc = mapper.createObjectNode();
                datosRuc.put("estado", xContRUC.path("dDesEstCons").asText());
                datosRuc.put("nombre", xContRUC.path("dRazCons").asText());
                datosRuc.put("RUCdv", RUCdv);
                datosRuc.put("numeroRUC", Long.parseLong(dRUCCons));
                datosRuc.put("facturaElectronica", xContRUC.path("dRUCFactElec").asText());
                datosRuc.put("codigoEstado", xContRUC.path("dCodEstCons").asText());

                response.set("datosRUC", datosRuc);

            }
            // Caso RUC no existe
            else if ("0500".equals(dCodRes)) {
                response.put("mensaje", "RUC no existe");
                response.putNull("datosRUC");
            }
            // Caso inesperado
            else {
                response.put("error", "Respuesta inesperada: " + dMsgRes);
            }

            return response;

        } catch (Exception e) {
            return mapper.createObjectNode()
                    .put("error", "Error consultando RUC: " + e.getMessage());
        }
    }



}
