package org.sifenboot.core.factura.service.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.sifenboot.core.factura.service.helper.JsonUtils;
import org.sifenboot.core.shared.AfectacionIVA;
import org.sifenboot.core.shared.UnidadMedida;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Component
public class DetallesJsonProcessor {

    private static final Logger log =
            LoggerFactory.getLogger(DetallesJsonProcessor.class);

    public static final JsonNode process(JsonNode facturaJson) {
        // 1. Control de seguridad: Validamos que el nodo raíz sea un objeto editable
        if (!(facturaJson instanceof ObjectNode json)) {
            return facturaJson;
        }

        // 2. Apuntamos al array de ítems
        JsonNode detallesNode = json.get("items");
        if (detallesNode == null || !detallesNode.isArray()) {
            return json; // Si no hay ítems, terminamos
        }

        // 2.1. Extraemos los indicadores de la raíz necesarios para dTotOpeItem
        int iTiDE = json.has("iTiDE") ? json.get("iTiDE").asInt() : 1;
        int iTImp = json.has("iTImp") ? json.get("iTImp").asInt() : 1;

        ArrayNode detallesArray = (ArrayNode) detallesNode;

        // 3. Recorremos cada hijo (ítem) del array
        for (JsonNode itemNode : detallesArray) {
            if (itemNode instanceof ObjectNode item) {

                // ADD cUniMed
                if (item.has("dDesUniMed")) {
                    String representante = item.get("dDesUniMed").asText();

                    UnidadMedida unidad = UnidadMedida.obtenerCodigo(representante);

                    if (unidad != null) {
                        item.put("cUniMed", unidad.getCodigo());
                    }
                }


                // ADD dTotBruOpeItem
                // ADD dTotOpeItem
                if (item.has("dPUniProSer") && item.has("dCantProSer")) {
                    String precioRaw = item.get("dPUniProSer").asText();
                    String cantRaw = item.get("dCantProSer").asText();

                    try {
                        BigDecimal precio = new BigDecimal(precioRaw);
                        BigDecimal cantidad = new BigDecimal(cantRaw);

                        BigDecimal totalBruto = precio.multiply(cantidad);
                        String totalBrutoStr = totalBruto.stripTrailingZeros().toPlainString();

                        item.put("dTotBruOpeItem", totalBrutoStr);

                        // 6. CÁLCULO: dTotOpeItem (Valor total de la operación por ítem)
                        BigDecimal totalOperacion;

                        if (iTiDE == 4) {
                            // Regla para Autofactura
                            totalOperacion = totalBruto;
                        } else if (iTImp == 1 || iTImp == 3 || iTImp == 4 || iTImp == 5) {
                            // Regla general SIFEN con deducciones aplicadas al precio unitario
                            BigDecimal dDescItem = JsonUtils.getBigDecimalOrZero(item, "dDescItem");
                            BigDecimal dDescGloItem = JsonUtils.getBigDecimalOrZero(item, "dDescGloItem");
                            BigDecimal dAntPreUniIt = JsonUtils.getBigDecimalOrZero(item, "dAntPreUniIt");
                            BigDecimal dAntGloPreUniIt = JsonUtils.getBigDecimalOrZero(item, "dAntGloPreUniIt");

                            BigDecimal precioNeto = precio.subtract(dDescItem)
                                    .subtract(dDescGloItem)
                                    .subtract(dAntPreUniIt)
                                    .subtract(dAntGloPreUniIt);

                            totalOperacion = precioNeto.multiply(cantidad);
                        } else {
                            totalOperacion = totalBruto;
                        }

                        item.put("dTotOpeItem", totalOperacion.stripTrailingZeros().toPlainString());

                    } catch (NumberFormatException e) {
                        log.error("Error al calcular montos del ítem. Valores no numéricos - Precio: '{}', Cantidad: '{}'",
                                precioRaw, cantRaw);
                    }
                }

                // //ADD dDesAfecIVA
                // ADD dDesAfecIVA
                int codigoAfec = 1; // Valor por defecto: Gravado IVA

                if (item.has("iAfecIVA")) {
                    codigoAfec = item.get("iAfecIVA").asInt();
                } else {
                    item.put("iAfecIVA", codigoAfec);
                }

                try {
                    String descripcion = AfectacionIVA.obtenerDescripcion(codigoAfec);

                    if (descripcion != null) {
                        item.put("dDesAfecIVA", descripcion);
                    } else {
                        log.warn(
                                "Código de iAfecIVA desconocido: {}",
                                codigoAfec
                        );
                    }
                } catch (Exception e) {
                    log.error(
                            "Error al procesar iAfecIVA en el ítem",e
                    );
                }


                // ADD dPropIVA
                JsonNode dPropIVANode = item.get("dPropIVA");
                if (dPropIVANode == null || dPropIVANode.asText().isBlank()) {
                    item.put("dPropIVA", 100);
                }


                // ADD dTasaIVA
                JsonNode dTasaIVANode = item.get("dTasaIVA");
                if (dTasaIVANode == null || dTasaIVANode.asText().isBlank()) {

                    codigoAfec = item.has("iAfecIVA")
                            ? item.get("iAfecIVA").asInt()
                            : 1;

                    int tasaIVA;
                    // Exonerado o Exento
                    if (codigoAfec == 2 || codigoAfec == 3) {
                        tasaIVA = 0;
                    } else {
                        // Gravado o Gravado parcial
                        tasaIVA = 10;
                    }
                    item.put("dTasaIVA", tasaIVA);
                }


                // ADD dBasGravIVA
                try {
                    BigDecimal dBasGravIVA = BigDecimal.ZERO;

                    codigoAfec = item.has("iAfecIVA")
                            ? item.get("iAfecIVA").asInt()
                            : 1;

                    // Solo aplica para Gravado o Gravado parcial
                    if (codigoAfec == 1 || codigoAfec == 4) {

                        BigDecimal totalOperacion = JsonUtils.getBigDecimalOrZero(item, "dTotOpeItem");
                        BigDecimal proporcionIVA = JsonUtils.getBigDecimalOrZero(item, "dPropIVA");

                        int tasaIVA = item.has("dTasaIVA")
                                ? item.get("dTasaIVA").asInt()
                                : 10;

                        // dTotOpeItem * (dPropIVA / 100)
                        BigDecimal montoGravado =
                                totalOperacion.multiply(
                                        proporcionIVA.divide(
                                                BigDecimal.valueOf(100),
                                                10,
                                                RoundingMode.HALF_UP
                                        )
                                );

                        BigDecimal divisor;
                        if (tasaIVA == 5) {
                            divisor = BigDecimal.valueOf(1.05);
                        } else {
                            // Default 10%
                            divisor = BigDecimal.valueOf(1.10);
                        }

                        dBasGravIVA = montoGravado.divide(
                                divisor,
                                2,
                                RoundingMode.HALF_UP
                        );
                    }

                    item.put("dBasGravIVA",
                            dBasGravIVA.stripTrailingZeros().toPlainString()
                    );

                } catch (Exception e) {
                    log.error("Error al calcular dBasGravIVA", e);
                }


                // ADD dLiqIVAItem
                try {
                    BigDecimal dLiqIVAItem = BigDecimal.ZERO;
                    codigoAfec = item.has("iAfecIVA")
                            ? item.get("iAfecIVA").asInt()
                            : 1;

                    // Solo aplica para Gravado o Gravado parcial
                    if (codigoAfec == 1 || codigoAfec == 4) {

                        BigDecimal baseGravada = JsonUtils.getBigDecimalOrZero(item, "dBasGravIVA");
                        BigDecimal tasaIVA = JsonUtils.getBigDecimalOrZero(item, "dTasaIVA");

                        dLiqIVAItem = baseGravada.multiply(
                                tasaIVA.divide(
                                        BigDecimal.valueOf(100),
                                        10,
                                        RoundingMode.HALF_UP
                                )
                        );
                    }

                    item.put(
                            "dLiqIVAItem",
                            dLiqIVAItem
                                    .setScale(2, RoundingMode.HALF_UP)
                                    .stripTrailingZeros()
                                    .toPlainString()
                    );

                } catch (Exception e) {
                    log.error( "Error al calcular dLiqIVAItem",e );
                }


                // ADD dBasExe
                try {

                    BigDecimal dBasExe = BigDecimal.ZERO;
                    codigoAfec = item.has("iAfecIVA")
                            ? item.get("iAfecIVA").asInt()
                            : 1;

                    // Solo aplica para Gravado parcial
                    if (codigoAfec == 4) {

                        BigDecimal dTotOpeItem = JsonUtils.getBigDecimalOrZero(item, "dTotOpeItem");

                        BigDecimal dPropIVA = JsonUtils.getBigDecimalOrZero(item, "dPropIVA");

                        BigDecimal dTasaIVA = JsonUtils.getBigDecimalOrZero(item, "dTasaIVA");

                        // 100 * dTotOpeItem * (100 - dPropIVA)
                        BigDecimal numerador =
                                BigDecimal.valueOf(100)
                                        .multiply(dTotOpeItem)
                                        .multiply(BigDecimal.valueOf(100)
                                                        .subtract(dPropIVA));

                        // 10000 + (dTasaIVA * dPropIVA)
                        BigDecimal denominador =
                                BigDecimal.valueOf(10000)
                                        .add(dTasaIVA.multiply(dPropIVA));

                        dBasExe = numerador.divide(
                                denominador,
                                2,
                                RoundingMode.HALF_UP
                        );
                    }

                    item.put("dBasExe",
                            dBasExe.stripTrailingZeros().toPlainString());

                } catch (Exception e) {
                    log.error("Error al calcular dBasExe", e );
                }


            }
        }

        return json;
    }


}