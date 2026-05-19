package org.sifenboot.core.factura.service.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.sifenboot.app.admin.emisor.model.Emisor;
import org.sifenboot.core.factura.service.helper.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
public class ImpuestoJsonProcessor {

    private static final Logger log = LoggerFactory.getLogger(ImpuestoJsonProcessor.class);

    public static final JsonNode process(JsonNode facturaJson) {

        if (!(facturaJson instanceof ObjectNode)) {
            return null;
        }
        ObjectNode json = (ObjectNode) facturaJson;

        JsonNode detallesNode = json.get("items");
        if (detallesNode == null || !detallesNode.isArray()) {
            return json;
        }

        ArrayNode detallesArray = (ArrayNode) detallesNode;

        // Variables de acumulación para los ítems
        BigDecimal sumIva5 = BigDecimal.ZERO;
        BigDecimal sumIva10 = BigDecimal.ZERO;
        BigDecimal dBaseGrav5 = BigDecimal.ZERO;
        BigDecimal dBaseGrav10 = BigDecimal.ZERO;

        boolean hasIva5 = false;
        boolean hasIva10 = false;

        // 1. Recorremos los ítems para calcular IVA y Bases Gravadas
        for (JsonNode itemNode : detallesArray) {
            String tasaIva = itemNode.path("dTasaIVA").asText("").trim();

            BigDecimal liqIvaItem = JsonUtils.getBigDecimalOrZero(itemNode, "dLiqIVAItem");
            BigDecimal basGravIva = JsonUtils.getBigDecimalOrZero(itemNode, "dBasGravIVA");

            if ("5".equals(tasaIva)) {
                sumIva5 = sumIva5.add(liqIvaItem);
                dBaseGrav5 = dBaseGrav5.add(basGravIva);
                hasIva5 = true;
            } else if ("10".equals(tasaIva)) {
                sumIva10 = sumIva10.add(liqIvaItem);
                dBaseGrav10 = dBaseGrav10.add(basGravIva);
                hasIva10 = true;
            }
        }

        // 2. Validación de iTImp para la presencia de campos de IVA
        int iTImp = json.path("iTImp").asInt(-1);

        if (iTImp == 1 || iTImp == 5) {

            // Recuperamos valores necesarios de la raíz
            BigDecimal dRedon = JsonUtils.getBigDecimalOrZero(json, "dRedon");
            BigDecimal dIVAComi = JsonUtils.getBigDecimalOrZero(json, "dIVAComi");

            // Cálculo de dLiqTotIVA5 (Redondeo / 21) si existe tasa 5%
            BigDecimal dLiqTotIVA5 = BigDecimal.ZERO;
            if (hasIva5 && dRedon.compareTo(BigDecimal.ZERO) > 0) {
                // (Valor redondeo / 1.05) * 0.05 es exactamente dRedon / 21
                dLiqTotIVA5 = dRedon.divide(new BigDecimal("21"), 0, RoundingMode.HALF_UP);
            }

            // Cálculo de dLiqTotIVA10 (Redondeo / 11) si existe tasa 10%
            BigDecimal dLiqTotIVA10 = BigDecimal.ZERO;
            if (hasIva10 && dRedon.compareTo(BigDecimal.ZERO) > 0) {
                // (Valor redondeo / 1.1) * 0.10 es exactamente dRedon / 11
                dLiqTotIVA10 = dRedon.divide(new BigDecimal("11"), 0, RoundingMode.HALF_UP);
            }

            // Cálculo aritmético: dTotIVA = dIVA5 + dIVA10 - dLiqTotIVA5 - dLiqTotIVA10 + dIVAComi
            BigDecimal dTotIVA = sumIva5.add(sumIva10)
                    .subtract(dLiqTotIVA5)
                    .subtract(dLiqTotIVA10)
                    .add(dIVAComi);

            // Cálculo aritmético: dTBasGraIVA = dBaseGrav5 + dBaseGrav10
            BigDecimal dTBasGraIVA = dBaseGrav5.add(dBaseGrav10);

            // Inyección de campos en el JSON
            json.put("dIVA5", sumIva5);
            json.put("dIVA10", sumIva10);
            json.put("dLiqTotIVA5", dLiqTotIVA5);
            json.put("dLiqTotIVA10", dLiqTotIVA10);
            json.put("dTotIVA", dTotIVA);
            json.put("dBaseGrav5", dBaseGrav5);
            json.put("dBaseGrav10", dBaseGrav10);
            json.put("dTBasGraIVA", dTBasGraIVA);

        } else {
            // Remoción estricta si iTImp no es ni 1 ni 5
            json.remove("dIVA5");
            json.remove("dIVA10");
            json.remove("dLiqTotIVA5");
            json.remove("dLiqTotIVA10");
            json.remove("dTotIVA");
            json.remove("dBaseGrav5");
            json.remove("dBaseGrav10");
            json.remove("dTBasGraIVA");
        }

        // 3. Cálculo de dTotGralOpe (Independiente de iTImp)
        BigDecimal dTotOpe = JsonUtils.getBigDecimalOrZero(json, "dTotOpe");
        BigDecimal dRedon = JsonUtils.getBigDecimalOrZero(json, "dRedon");
        BigDecimal dComi = JsonUtils.getBigDecimalOrZero(json, "dComi");

        BigDecimal dTotGralOpe = dTotOpe.subtract(dRedon).add(dComi);
        json.put("dTotGralOpe", dTotGralOpe);

        return json;
    }
}