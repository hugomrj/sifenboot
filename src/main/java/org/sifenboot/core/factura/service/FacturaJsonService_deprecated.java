package org.sifenboot.core.factura.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.sifenboot.app.admin.emisor.model.Emisor;
import org.sifenboot.app.admin.emisor.service.EmisorService;
import org.sifenboot.core.integration.builder.CodigoSeguridadGenerator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;


// @Service
public class FacturaJsonService_deprecated {

    private final EmisorService emisorService;
    private final CodigoSeguridadGenerator codigoSeguridadGenerator;

    public FacturaJsonService_deprecated(
            EmisorService emisorService,
            CodigoSeguridadGenerator codigoSeguridadGenerator
    ) {
        this.emisorService = emisorService;
        this.codigoSeguridadGenerator = codigoSeguridadGenerator;
    }

    public JsonNode completarDatosEmisor(String codEmisor, JsonNode facturaInput) {

        Emisor emisor = emisorService.findByCodEmisor(codEmisor);
        ObjectNode json = facturaInput.deepCopy();

        // iTipEmi
        if (!json.has("iTipEmi")) {
            json.put("iTipEmi", 1);
        }

        // dDesTipEmi
        if (!json.has("dDesTipEmi")) {
            json.put("dDesTipEmi", "Normal");
        }

        // dCodSeg
        JsonNode dCodSegNode = json.get("dCodSeg");
        if (dCodSegNode == null || dCodSegNode.asText().isBlank()) {
            json.put("dCodSeg", codigoSeguridadGenerator.generar());
        }

        // iTiDE
        if (!json.has("iTiDE")) {
            json.put("iTiDE", 1);
        }

        // dDesTiDE
        if (!json.has("dDesTiDE")) {
            json.put("dDesTiDE", "Factura electrónica");
        }

        // dNumTim
        JsonNode dNumTimNode = json.get("dNumTim");
        if (dNumTimNode == null || dNumTimNode.asText().isBlank()) {
            json.put("dNumTim", emisor.getNumeroTimbrado());
        }

        // --- Datos del Emisor SIFEN ---

        if (!json.has("dRucEm")) {
            // SIFEN requiere el RUC sin DV en dRucEm. Si lo guardás como String con guión, limpialo aquí.
            json.put("dRucEm", emisor.getRuc());
        }

        if (!json.has("dDVEmi")) {
            json.put("dDVEmi", emisor.getRucDv());
        }

        if (!json.has("iTipCont")) {
            json.put("iTipCont", emisor.getTipoContribuyente());
        }

        if (!json.has("dNomEmi")) {
            json.put("dNomEmi", emisor.getRazonSocial());
        }

        if (!json.has("dNomFanEmi") && emisor.getNombreFantasia() != null) {
            json.put("dNomFanEmi", emisor.getNombreFantasia());
        }

        if (!json.has("dDirEmi")) {
            json.put("dDirEmi", emisor.getDireccion());
        }

        if (!json.has("dNumCas")) {
            json.put("dNumCas", emisor.getNumeroCasa() != null ? emisor.getNumeroCasa() : 0);
        }

        // Ubicación (Departamento)
        if (emisor.getDepartamento() != null) {
            if (!json.has("cDepEmi")) {
                json.put("cDepEmi", emisor.getDepartamento().getId()); // O el código que uses para la SET
            }
            if (!json.has("dDesDepEmi")) {
                json.put("dDesDepEmi", emisor.getDepartamento().getDescripcion());
            }
        }
        // Ubicación (Distrito)
        if (emisor.getDistrito() != null) {

            if (!json.has("cDisEmi")) {
                json.put("cDisEmi", emisor.getDistrito().getId() );
            }

            if (!json.has("dDesDisEmi")) {
                json.put("dDesDisEmi", emisor.getDistrito().getDescripcion());
            }
        }

        // Localidad / Ciudad
        if (emisor.getLocalidad() != null) {

            if (!json.has("cCiuEmi")) {
                json.put("cCiuEmi", emisor.getLocalidad().getCodigoLocalidad());
            }

            if (!json.has("dDesCiuEmi")) {
                json.put("dDesCiuEmi", emisor.getLocalidad().getDescripcion());
            }
        }

        // Contacto
        if (!json.has("dTelEmi") && emisor.getTelefono() != null) {
            json.put("dTelEmi", emisor.getTelefono());
        }

        if (!json.has("dEmailE") && emisor.getEmail() != null) {
            json.put("dEmailE", emisor.getEmail());
        }

        // Actividad Económica
        if (!json.has("cActEco") && emisor.getActividadEconomicaCodigo() != null) {
            json.put("cActEco", emisor.getActividadEconomicaCodigo());
        }

        if (!json.has("dDesActEco") && emisor.getActividadEconomicaDescripcion() != null) {
            json.put("dDesActEco", emisor.getActividadEconomicaDescripcion());
        }

        return json;
    }


    public JsonNode calcularTotalesFactura(JsonNode facturaNode) {

        ObjectNode factura = (ObjectNode) facturaNode;
        ArrayNode detalles = (ArrayNode) factura.path("items");

        // --- 1. INICIALIZAR ACUMULADORES (Variables para los Totales) ---
        BigDecimal g_subExe = BigDecimal.ZERO; // Acumula Exentos (iAfecIVA = 3)
        BigDecimal g_subExo = BigDecimal.ZERO; // Acumula Exonerados (iAfecIVA = 2)
        BigDecimal g_sub5   = BigDecimal.ZERO; // Acumula Gravados 5%
        BigDecimal g_sub10  = BigDecimal.ZERO; // Acumula Gravados 10%

        BigDecimal g_iva5   = BigDecimal.ZERO;
        BigDecimal g_iva10  = BigDecimal.ZERO;

        BigDecimal g_base5  = BigDecimal.ZERO;
        BigDecimal g_base10 = BigDecimal.ZERO;

        // Constantes para extraer IVA del precio final (Precio con IVA incluido)
        // IVA 10%: Precio / 11
        // IVA 5%:  Precio / 21
        final BigDecimal DIV_10 = new BigDecimal("11");
        final BigDecimal DIV_5  = new BigDecimal("21");

        // --- 2. PROCESAR EL DETALLE (Calcula cada ítem y acumula) ---
        if (detalles != null) {
            for (JsonNode itemNode : detalles) {
                ObjectNode item = (ObjectNode) itemNode;

                // A. Obtener datos básicos del ítem
                BigDecimal cantidad = new BigDecimal(item.path("dCantProSer").asText("0"));
                BigDecimal precioUni = new BigDecimal(item.path("dPUniProSer").asText("0"));

                // Manejo de descuentos/anticipos del ítem (si existen en el JSON)
                BigDecimal descuento = item.has("dDescItem") ? new BigDecimal(item.path("dDescItem").asText("0")) : BigDecimal.ZERO;
                BigDecimal anticipo  = item.has("dAntPreItem") ? new BigDecimal(item.path("dAntPreItem").asText("0")) : BigDecimal.ZERO;

                // B. Calcular Total del Ítem (EA008)
                // Fórmula: (Precio - Descuento - Anticipo) * Cantidad
                BigDecimal baseCalculo = precioUni.subtract(descuento).subtract(anticipo);
                BigDecimal totalItem = baseCalculo.multiply(cantidad).setScale(0, RoundingMode.HALF_EVEN);

                // Actualizamos el ítem en el JSON
                item.put("dTotBruOpeItem", totalItem.toString());
                item.put("dTotOpeItem", totalItem.toString());

                // C. Clasificar y Acumular según tipo de afectación (E731 / iAfecIVA)
                int afectacion = item.path("iAfecIVA").asInt(1); // Por defecto 1 (Gravado)

                if (afectacion == 3) {
                    // --- EXENTO ---
                    // No tiene IVA, ni base gravada
                    item.put("dBasGravIVA", "0");
                    item.put("dLiqIVAItem", "0");
                    item.put("dBasExe", totalItem.toString()); // Base Exenta = Total Item

                    g_subExe = g_subExe.add(totalItem); // Suma al subtotal Exento

                } else if (afectacion == 2) {
                    // --- EXONERADO ---
                    // La operación existe pero no genera crédito fiscal por exoneración
                    item.put("dBasGravIVA", "0");
                    item.put("dLiqIVAItem", "0");
                    item.put("dBasExe", "0");

                    g_subExo = g_subExo.add(totalItem); // Suma al subtotal Exonerado

                } else {
                    // --- GRAVADO (iAfecIVA = 1) ---
                    // Aquí se calcula el IVA (IVA Devengado)
                    item.put("dBasExe", "0");
                    int tasa = item.path("dTasaIVA").asInt(10);

                    BigDecimal ivaItem = BigDecimal.ZERO;
                    BigDecimal baseItem = BigDecimal.ZERO;

                    if (tasa == 10) {
                        ivaItem = totalItem.divide(DIV_10, 0, RoundingMode.HALF_EVEN);
                        baseItem = totalItem.subtract(ivaItem);

                        // Acumular a los totales de 10%
                        g_sub10 = g_sub10.add(totalItem);
                        g_iva10 = g_iva10.add(ivaItem);
                        g_base10 = g_base10.add(baseItem);

                    } else if (tasa == 5) {
                        ivaItem = totalItem.divide(DIV_5, 0, RoundingMode.HALF_EVEN);
                        baseItem = totalItem.subtract(ivaItem);

                        // Acumular a los totales de 5%
                        g_sub5 = g_sub5.add(totalItem);
                        g_iva5 = g_iva5.add(ivaItem);
                        g_base5 = g_base5.add(baseItem);
                    }

                    // Actualizamos el ítem con el IVA y Base calculados
                    item.put("dBasGravIVA", baseItem.toString());
                    item.put("dLiqIVAItem", ivaItem.toString());
                }
            }
        }

        // --- 3. ACTUALIZAR TOTALES DE LA CABECERA ---

        // Subtotales por grupo
        factura.put("dSubExe", g_subExe.toString());
        factura.put("dSubExo", g_subExo.toString());
        factura.put("dSub5", g_sub5.toString());
        factura.put("dSub10", g_sub10.toString());

        // Liquidación de IVA
        factura.put("dIVA5", g_iva5.toString());
        factura.put("dIVA10", g_iva10.toString());
        factura.put("dTotIVA", g_iva5.add(g_iva10).toString());

        // Bases Gravadas (para el reporte del IVA)
        factura.put("dBaseGrav5", g_base5.toString());
        factura.put("dBaseGrav10", g_base10.toString());
        factura.put("dTBasGraIVA", g_base5.add(g_base10).toString());

        // Total General de la Operación
        BigDecimal totalGral = g_subExe.add(g_subExo).add(g_sub5).add(g_sub10);
        factura.put("dTotOpe", totalGral.toString());
        factura.put("dTotGralOpe", totalGral.toString());

        // Limpieza de campos opcionales que no se usaron en este cálculo simple
        if (factura.path("dTotDesc").isMissingNode()) factura.put("dTotDesc", "0");
        if (factura.path("dAnticipo").isMissingNode()) factura.put("dAnticipo", "0");

        return factura;
    }


}