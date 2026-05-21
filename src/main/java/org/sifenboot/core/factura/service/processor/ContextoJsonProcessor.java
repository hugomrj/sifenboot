package org.sifenboot.core.factura.service.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.sifenboot.core.factura.service.parser.SifenFechaParser;
import org.sifenboot.core.shared.*;
import org.sifenboot.core.integration.util.io.StringUtils; // Asumiendo que usas tu utilitario
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextoJsonProcessor {

    public static final Logger log = LoggerFactory.getLogger(ContextoJsonProcessor.class);

    public static final JsonNode process(JsonNode facturaJson) {

        if (!(facturaJson instanceof ObjectNode json)) {
            return facturaJson;
        }

        // =========================================================================
        // 1. PROCESAMIENTO DE MONEDA
        // =========================================================================
        String monedaCodigo = "PYG";

        if (json.has("cMoneOpe") && !json.get("cMoneOpe").isNull()) {
            String inputMoneda = json.get("cMoneOpe").asText().trim().toUpperCase();
            if (MonedaISO.isValid(inputMoneda)) {
                monedaCodigo = inputMoneda;
            }
        } else if (json.has("cMoneTiPag") && !json.get("cMoneTiPag").isNull()) {
            String inputMoneda = json.get("cMoneTiPag").asText().trim().toUpperCase();
            if (MonedaISO.isValid(inputMoneda)) {
                monedaCodigo = inputMoneda;
            }
        }

        String monedaDescripcion = MonedaISO.getDescripcionOrDefault(monedaCodigo);

        json.put("cMoneOpe", monedaCodigo);
        json.put("dDesMoneOpe", monedaDescripcion);

        json.put("cMoneTiPag", monedaCodigo);
        json.put("dDMoneTiPag", monedaDescripcion);

        // =========================================================================
        // 2. PROCESAMIENTO DE TIPO DE TRANSACCIÓN
        // =========================================================================
        int tipoTransaccionCodigo = 1;
        if (json.has("iTipTra") && !json.get("iTipTra").isNull()) {
            tipoTransaccionCodigo = json.get("iTipTra").asInt();
        }
        json.put("iTipTra", tipoTransaccionCodigo);
        json.put("dDesTipTra", TipoTransaccion.getDescripcionPorCodigo(tipoTransaccionCodigo));

        // =========================================================================
        // 3. PROCESAMIENTO DE TIPO DE IMPUESTO
        // =========================================================================
        int tipoImpuestoCodigo = 1;
        if (json.has("iTImp") && !json.get("iTImp").isNull()) {
            tipoImpuestoCodigo = json.get("iTImp").asInt();
        }
        json.put("iTImp", tipoImpuestoCodigo);
        json.put("dDesTImp", TipoImpuesto.getDescripcionPorCodigo(tipoImpuestoCodigo));

        // =========================================================================
        // 4. PROCESAMIENTO DE FECHA DE EMISIÓN DE DE (dFeEmiDE)
        // =========================================================================
        String fechaInput = (json.has("dFeEmiDE") && !json.get("dFeEmiDE").isNull())
                ? json.get("dFeEmiDE").asText().trim()
                : null;
        json.put("dFeEmiDE", SifenFechaParser.normalizar(fechaInput));

        // =========================================================================
        // 5. PROCESAMIENTO DE TIPO DE PAGO
        // =========================================================================
        int tipoPagoCodigo = 1;
        if (json.has("iTiPago") && !json.get("iTiPago").isNull()) {
            tipoPagoCodigo = json.get("iTiPago").asInt();
        }
        json.put("iTiPago", tipoPagoCodigo);
        json.put("dDesTiPag", TipoPago.getDescripcionPorCodigo(tipoPagoCodigo));

        // =========================================================================
        // 6. INDICADOR DE PRESENCIA (iIndPres)
        // =========================================================================
        int presenciaCodigo = 1;
        if (json.has("iIndPres") && !json.get("iIndPres").isNull()) {
            presenciaCodigo = json.get("iIndPres").asInt();
        }
        json.put("iIndPres", presenciaCodigo);
        json.put("dDesIndPres", IndicadorPresencia.getDescripcionPorCodigo(presenciaCodigo));

        // =========================================================================
        // 7. PAÍS DEL RECEPTOR (cPaisRec)
        // =========================================================================
        String paisCodigo = "PRY";
        if (json.has("cPaisRec") && !json.get("cPaisRec").isNull()) {
            paisCodigo = json.get("cPaisRec").asText().trim().toUpperCase();
        }
        json.put("cPaisRec", paisCodigo);
        json.put("dDesPaisRe", PaisISO.getDescripcionOrDefault(paisCodigo));

        // =========================================================================
        // 8. CONDICIÓN DE LA OPERACIÓN (iCondOpe)
        // =========================================================================
        int condicionCodigo = 1;
        if (json.has("iCondOpe") && !json.get("iCondOpe").isNull()) {
            condicionCodigo = json.get("iCondOpe").asInt();
        }
        json.put("iCondOpe", condicionCodigo);
        json.put("dDCondOpe", (condicionCodigo == 2) ? "Crédito" : "Contado");

        // =========================================================================
        // 9. FORMATEO DE NUMERACIÓN (Ceros a la izquierda para dEst, dPunExp, dNumDoc)
        // =========================================================================

        // Establecimiento (3 dígitos)
        if (json.has("dEst") && !json.get("dEst").isNull()) {
            String estRaw = json.get("dEst").asText().trim();
            json.put("dEst", leftPad(estRaw, '0', 3));
        }

        // Punto de Expedición (3 dígitos)
        if (json.has("dPunExp") && !json.get("dPunExp").isNull()) {
            String punExpRaw = json.get("dPunExp").asText().trim();
            json.put("dPunExp", leftPad(punExpRaw, '0', 3));
        }

        // Número de Documento (7 dígitos)
        if (json.has("dNumDoc") && !json.get("dNumDoc").isNull()) {
            String numDocRaw = json.get("dNumDoc").asText().trim();
            json.put("dNumDoc", leftPad(numDocRaw, '0', 7));
        }

        return json;
    }

    // Método utilitario interno para evitar fallas si no deseas importar dependencias externas
    private static String leftPad(String input, char padChar, int length) {
        if (input == null) return "";
        StringBuilder sb = new StringBuilder(input);
        while (sb.length() < length) {
            sb.insert(0, padChar);
        }
        return sb.toString();
    }
}