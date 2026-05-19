package org.sifenboot.core.factura.service.mapping;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonSifenMapping {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static JsonNode process(JsonNode erpJson) {
        if (!(erpJson instanceof ObjectNode)) {
            return erpJson;
        }

        ObjectNode sifenJson = mapper.createObjectNode();

        // 1. Mapeo Raíz (De amigable a SIFEN)
        if (erpJson.has("tipo_emision"))
            sifenJson.set("iTipEmi", erpJson.get("tipo_emision"));
        if (erpJson.has("tipo_documento"))     sifenJson.set("iTiDE", erpJson.get("tipo_documento"));
        if (erpJson.has("tipo_impuesto"))      sifenJson.set("iTImp", erpJson.get("tipo_impuesto"));
        if (erpJson.has("total_operacion"))    sifenJson.set("dTotOpe", erpJson.get("total_operacion"));
        if (erpJson.has("redondeo"))           sifenJson.set("dRedon", erpJson.get("redondeo"));
        if (erpJson.has("comision"))           sifenJson.set("dComi", erpJson.get("comision"));
        if (erpJson.has("iva_comision"))       sifenJson.set("dIVAComi", erpJson.get("iva_comision"));

        // 2. Mapeo de Ítems
        JsonNode itemsErp = erpJson.get("items");
        if (itemsErp != null && itemsErp.isArray()) {
            ArrayNode itemsSifenArray = mapper.createArrayNode();

            for (JsonNode itemNode : itemsErp) {
                ObjectNode itemSifen = mapper.createObjectNode();

                if (itemNode.has("codigo"))          itemSifen.set("dCodInt", itemNode.get("codigo"));
                if (itemNode.has("descripcion"))     itemSifen.set("dDesProSer", itemNode.get("descripcion"));
                if (itemNode.has("cantidad"))        itemSifen.set("dCantProSer", itemNode.get("cantidad"));
                if (itemNode.has("precio_unitario")) itemSifen.set("dPUniProSer", itemNode.get("precio_unitario"));
                if (itemNode.has("tasa_iva"))        itemSifen.set("dTasaIVA", itemNode.get("tasa_iva"));
                if (itemNode.has("liquidacion_iva")) itemSifen.set("dLiqIVAItem", itemNode.get("liquidacion_iva"));
                if (itemNode.has("base_gravada"))    itemSifen.set("dBasGravIVA", itemNode.get("base_gravada"));

                itemsSifenArray.add(itemSifen);
            }
            sifenJson.set("items", itemsSifenArray);
        }

        return sifenJson;
    }
}