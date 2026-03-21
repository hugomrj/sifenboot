package com.sifen.api.repository.cancelar;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sifen.api.integration.soap.client.EventoCancelarClient;
import com.sifen.api.integration.util.message.SoapBodyExtractor;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Repository;

import java.net.http.HttpResponse;
@Repository
public class EventoCancelarRepository {

    private final EventoCancelarClient cancelarClient;

    public EventoCancelarRepository(EventoCancelarClient cancelarClient) {
        this.cancelarClient = cancelarClient;
    }

    public String cancelarEvento(String cdc, String motivo) {

        try {
            HttpResponse<String> httpResponse =
                    cancelarClient.cancelarEvento(cdc, motivo);


            if (httpResponse.statusCode() >= 500) {
                throw new RuntimeException("Error HTTP " + httpResponse.statusCode());
            }

            return httpResponse.body(); // XML crudo

        } catch (Exception e) {
            throw new RuntimeException("Error llamando al servicio SOAP", e);
        }
    }
}
