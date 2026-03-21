package org.sifenboot.integration.util.message;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class SoapBodyExtractor {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String extractBody(String soapResponse) {
        try {
            // Limpia los prefijos típicos del SOAP
            String cleaned = soapResponse
                    .replace("ns2:", "")
                    .replace("env:", "");

            JsonNode root = mapper.readTree(cleaned);

            if (root.has("Envelope")) {
                JsonNode envelope = root.get("Envelope");

                if (envelope.has("Body")) {
                    return envelope.get("Body").toString();
                }
            }

            // Si no tiene estructura Envelope/Body, devolvemos el JSON limpio
            return cleaned;

        } catch (Exception e) {
            // Si no es JSON válido, devolvemos un fallback limpio
            return soapResponse
                    .replace("ns2:", "")
                    .replace("env:", "");
        }
    }
}

