package org.sifenboot.core.lote.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.json.XML;
import org.sifenboot.core.integration.soap.client.LoteClient;
import org.sifenboot.security.certificado.model.Certificado;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;

@Repository
public class LoteEnvioRepository {

    private final LoteClient client;
    private final ObjectMapper objectMapper;

    public LoteEnvioRepository(
            LoteClient client,
            ObjectMapper objectMapper
    ) {
        this.client = client;
        this.objectMapper = objectMapper;
    }

    public JsonNode enviarLote(
            List<String> xmls,
            Certificado certificado
    ) {

        String xmlLote = String.join("", xmls);

        HttpResponse<String> httpResponse =
                client.recibeLote(xmlLote, certificado);

        String xmlOutput = httpResponse.body();

        JSONObject jsonObject = XML.toJSONObject(xmlOutput);

        try {
            return objectMapper.readTree(jsonObject.toString());

        } catch (IOException e) {
            throw new RuntimeException(
                    "Error convirtiendo XML a JSON",
                    e
            );
        }
    }
}

