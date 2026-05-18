package org.sifenboot.core.factura.service.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.sifenboot.core.shared.MonedaISO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MonedaJsonProcessor {

    public static final Logger log = LoggerFactory.getLogger(TotalesJsonProcessor.class);

    public static final JsonNode process(JsonNode facturaJson) {

        if (!(facturaJson instanceof ObjectNode json)) {
            return facturaJson;
        }


        String monedaCodigo = "PYG"; // Valor por defecto del esquema nacional

        if (json.has("cMoneTiPag") && !json.get("cMoneTiPag").isNull()) {
            String inputMoneda = json.get("cMoneTiPag").asText().trim().toUpperCase();
            if (MonedaISO.isValid(inputMoneda)) {
                monedaCodigo = inputMoneda;
            }
        }

        json.put("cMoneTiPag", monedaCodigo);
        json.put("dDMoneTiPag", MonedaISO.getDescripcionOrDefault(monedaCodigo));

        return json;
    }


}
