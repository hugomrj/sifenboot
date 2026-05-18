package org.sifenboot.core.factura.service.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.sifenboot.core.factura.service.helper.JsonUtils;
import org.sifenboot.core.shared.MonedaISO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.StreamSupport;


@Component
public class TotalesJsonProcessor {

    private static final Logger log = LoggerFactory.getLogger(TotalesJsonProcessor.class);

    public static final JsonNode process(JsonNode facturaJson) {
        // 1. Control de seguridad: Validamos que el nodo raíz sea un objeto editable
        if (!(facturaJson instanceof ObjectNode json)) {
            return facturaJson;
        }

        try {
            // 2. Obtener el array de "items"
            JsonNode detallesNode = json.get("items");
            if (detallesNode == null || !detallesNode.isArray()) {
                return json; // Si no hay ítems, terminamos
            }

            // 3. Extraer variables de control de la raíz (iTImp e iTiDE)
            int iTImp = json.has("iTImp") ? json.get("iTImp").asInt() : 0;
            int iTiDE = json.has("iTiDE") ? json.get("iTiDE").asInt() : 0;

            // Recogemos la moneda que ya fue procesada e inyectada previamente por MonedaJsonProcessor
            String monedaCodigo = json.has("cMoneTiPag") ? json.get("cMoneTiPag").asText().trim() : "PYG";

            // 4. Inicializar acumuladores de subtotales, descuentos y anticipos
            BigDecimal subExe = BigDecimal.ZERO;
            BigDecimal subExo = BigDecimal.ZERO;
            BigDecimal sub5 = BigDecimal.ZERO;
            BigDecimal sub10 = BigDecimal.ZERO;

            BigDecimal totDesc = BigDecimal.ZERO;
            BigDecimal totDescGlotem = BigDecimal.ZERO;
            BigDecimal totAntItem = BigDecimal.ZERO;
            BigDecimal totAnt = BigDecimal.ZERO;
            BigDecimal monTiPag = BigDecimal.ZERO;

            // 5. Un solo recorrido sobre la lista de ítems (O(N))
            for (JsonNode item : detallesNode) {
                BigDecimal totOpeItem = JsonUtils.getBigDecimalOrZero(item, "dTotOpeItem");
                monTiPag = monTiPag.add(totOpeItem);

                // Acumulamos los descuentos por ítem (particular y global)
                totDesc = totDesc.add(JsonUtils.getBigDecimalOrZero(item, "dDescItem"));
                totDescGlotem = totDescGlotem.add(JsonUtils.getBigDecimalOrZero(item, "dDescGloItem"));

                // Acumulamos los anticipos por ítem (particular y global)
                totAntItem = totAntItem.add(JsonUtils.getBigDecimalOrZero(item, "dAntPreUniIt"));
                totAnt = totAnt.add(JsonUtils.getBigDecimalOrZero(item, "dAntGloPreUniIt"));

                JsonNode afecIvaNode = item.get("iAfecIVA");
                if (afecIvaNode != null) {
                    String afecIva = afecIvaNode.asText().trim();

                    // Si es Exenta (3)
                    if ("3".equals(afecIva)) {
                        subExe = subExe.add(totOpeItem);
                    }
                    // Si es Exonerada (2)
                    else if ("2".equals(afecIva)) {
                        subExo = subExo.add(totOpeItem);
                    }
                    // Si es Gravado IVA (1) o Gravado parcial (4)
                    else if ("1".equals(afecIva) || "4".equals(afecIva)) {
                        JsonNode tasaIvaNode = item.get("dTasaIVA");
                        if (tasaIvaNode != null) {
                            String tasaIva = tasaIvaNode.asText().trim();
                            if ("5".equals(tasaIva)) {
                                sub5 = sub5.add(totOpeItem);
                            } else if ("10".equals(tasaIva)) {
                                sub10 = sub10.add(totOpeItem);
                            }
                        }
                    }
                }
            }

            // ==========================================
            // 6. CÁLCULOS COMPUESTOS DE LA OPERACIÓN
            // ==========================================
            BigDecimal descTotal = totDesc.add(totDescGlotem);
            BigDecimal anticipo = totAntItem.add(totAnt);
            BigDecimal porcDescTotal = JsonUtils.getBigDecimalOrZero(json, "dPorcDescTotal");

            // CÁLCULO DE dTotOpe (Total Bruto)
            BigDecimal totOpe = BigDecimal.ZERO;
            if (iTiDE == 4) {
                totOpe = monTiPag;
            } else if (iTImp == 1 || iTImp == 3 || iTImp == 4 || iTImp == 5) {
                totOpe = subExe.add(subExo).add(sub5).add(sub10);
            }

            // CÁLCULO DE dRedon (Redondeo de SIFEN - Grupo F)
            BigDecimal redon = BigDecimal.ZERO;
            if ("PYG".equals(monedaCodigo)) {
                BigDecimal netoAntesRedondeo = totOpe.subtract(anticipo);
                if (netoAntesRedondeo.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal resto = netoAntesRedondeo.remainder(new BigDecimal("50"));
                    if (resto.compareTo(BigDecimal.ZERO) != 0) {
                        redon = resto.negate(); // Resta el excedente para ajustar a las reglas de la SET
                    }
                }
            } else {
                redon = JsonUtils.getBigDecimalOrZero(json, "dRedon");
            }

            // ==========================================
            // 7. INYECCIÓN DE LOS CAMPOS EN LA RAÍZ DEL JSON
            // ==========================================
            json.put("dSubExe", subExe.toString());
            json.put("dSubExo", subExo.toString());

            // dSub5 y dSub10 solo deben existir si iTImp == 1 (IVA)
            if (iTImp == 1) {
                json.put("dSub5", sub5.toString());
                json.put("dSub10", sub10.toString());
            } else {
                json.remove("dSub5");
                json.remove("dSub10");
            }

            json.put("dTotOpe", totOpe.toString());
            json.put("dTotDesc", totDesc.toString());
            json.put("dTotDescGlotem", totDescGlotem.toString());
            json.put("dTotAntItem", totAntItem.toString());
            json.put("dTotAnt", totAnt.toString());
            json.put("dPorcDescTotal", porcDescTotal.toString());
            json.put("dDescTotal", descTotal.toString());
            json.put("dAnticipo", anticipo.toString());
            json.put("dRedon", redon.toString());
            json.put("dMonTiPag", monTiPag.toString());

        } catch (Exception e) {
            log.error("Error al procesar el cálculo de subtotales y totales en TotalesJsonProcessor", e);
        }

        return json;
    }
}