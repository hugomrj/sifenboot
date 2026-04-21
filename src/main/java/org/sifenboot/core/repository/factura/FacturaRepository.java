package org.sifenboot.core.repository.factura;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sifenboot.core.integration.soap.client.LoteClient;
import org.json.JSONObject;
import org.json.XML;
import org.sifenboot.security.certificado.model.Certificado;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.http.HttpResponse;

@Repository
public class FacturaRepository {

    private final LoteClient client;
    private final ObjectMapper objectMapper;

    public FacturaRepository(LoteClient loteClient, ObjectMapper objectMapper) {
        this.client = loteClient;
        this.objectMapper = objectMapper;
    }

    public JsonNode enviarFactura(String xml, Certificado certificado) {

        HttpResponse<String> httpResponse = client.recibeLote(xml, certificado);

        String xmlOutput = httpResponse.body();

        JSONObject jsonObject = XML.toJSONObject(xmlOutput);

        try {
            return objectMapper.readTree(jsonObject.toString());
        } catch (IOException e) {
            throw new RuntimeException("Error al convertir XML a JSON", e);
        }
    }
}
