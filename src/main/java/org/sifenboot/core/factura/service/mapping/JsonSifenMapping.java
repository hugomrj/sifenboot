package org.sifenboot.core.factura.service.mapping;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonSifenMapping {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static JsonNode process(JsonNode erpJson) {
        if (!(erpJson instanceof ObjectNode root)) {
            return erpJson;
        }

        ObjectNode sifenJson = root.deepCopy();

        // =========================================================================
        // 1. MAPEO RAÍZ (Campos ERP legibles a esquema nativo SIFEN)
        // =========================================================================
        mapearCampo(root, sifenJson, "tipo_emision", "iTipEmi");
        mapearCampo(root, sifenJson, "tipo_documento", "iTiDE");
        mapearCampo(root, sifenJson, "tipo_impuesto", "iTImp");
        mapearCampo(root, sifenJson, "total_operacion", "dTotOpe");
        mapearCampo(root, sifenJson, "redondeo", "dRedon");
        mapearCampo(root, sifenJson, "comision", "dComi");
        mapearCampo(root, sifenJson, "iva_comision", "dIVAComi");

        // Datos del Timbrado / Numeración
        mapearCampo(root, sifenJson, "establecimiento", "dEst");
        mapearCampo(root, sifenJson, "punto_expedicion", "dPunExp");
        mapearCampo(root, sifenJson, "numero_documento", "dNumDoc");
        mapearCampo(root, sifenJson, "fecha_emision", "dFeEmiDE");
        mapearCampo(root, sifenJson, "codigo_seguridad", "dCodSeg");

        // Datos del Receptor
        mapearCampo(root, sifenJson, "naturaleza_receptor", "iNatRec");
        mapearCampo(root, sifenJson, "tipo_contribuyente_receptor", "iTiContRec");
        mapearCampo(root, sifenJson, "tipo_operacion", "iTiOpe");
        mapearCampo(root, sifenJson, "ruc_receptor", "dRucRec");
        mapearCampo(root, sifenJson, "dv_receptor", "dDVRec");
        mapearCampo(root, sifenJson, "nombre_receptor", "dNomRec");
        mapearCampo(root, sifenJson, "indicador_presencia", "iIndPres");

        // Condición y Pago
        mapearCampo(root, sifenJson, "condicion_operacion", "iCondOpe");
        mapearCampo(root, sifenJson, "tipo_pago", "iTiPago");

        // =========================================================================
        // 2. MAPEO DE ÍTEMS
        // =========================================================================
        JsonNode itemsErp = root.get("items");
        if (itemsErp != null && itemsErp.isArray()) {
            ArrayNode itemsSifenArray = mapper.createArrayNode();

            for (JsonNode itemNode : itemsErp) {
                if (itemNode instanceof ObjectNode item) {
                    ObjectNode itemSifen = item.deepCopy();

                    mapearCampo(item, itemSifen, "codigo", "dCodInt");
                    mapearCampo(item, itemSifen, "descripcion", "dDesProSer");
                    mapearCampo(item, itemSifen, "unidad_medida_descripcion", "dDesUniMed");
                    mapearCampo(item, itemSifen, "cantidad", "dCantProSer");
                    mapearCampo(item, itemSifen, "precio_unitario", "dPUniProSer");
                    mapearCampo(item, itemSifen, "afectacion_iva", "iAfecIVA");
                    mapearCampo(item, itemSifen, "tasa_iva", "dTasaIVA");
                    mapearCampo(item, itemSifen, "liquidacion_iva", "dLiqIVAItem");
                    mapearCampo(item, itemSifen, "base_gravada", "dBasGravIVA");

                    itemsSifenArray.add(itemSifen);
                }
            }
            sifenJson.set("items", itemsSifenArray);
        }

        return sifenJson;
    }

    private static void mapearCampo(JsonNode origen, ObjectNode destino, String campoErp, String campoSifen) {
        if (origen.has(campoErp)) {
            destino.set(campoSifen, origen.get(campoErp));
            destino.remove(campoErp);
        }
    }
}