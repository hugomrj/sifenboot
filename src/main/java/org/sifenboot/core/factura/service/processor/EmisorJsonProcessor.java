package org.sifenboot.core.factura.service.processor;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.sifenboot.app.admin.emisor.model.Emisor;
import org.sifenboot.app.admin.emisor.service.EmisorService;
import org.sifenboot.core.integration.builder.CodigoSeguridadGenerator;
import org.springframework.stereotype.Component;


@Component
public class EmisorJsonProcessor {

    public static JsonNode process(Emisor emisor , JsonNode facturaJson) {

        if (!(facturaJson instanceof ObjectNode)) {
            return null;
        }

        ObjectNode json = (ObjectNode) facturaJson;

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
            json.put("dCodSeg", CodigoSeguridadGenerator.generar());
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
            json.put("dNumTim", Integer.parseInt(emisor.getNumeroTimbrado()));
        }

        // dFeIniT (Fecha de inicio del Timbrado)
        if (!json.has("dFeIniT") && emisor.getFechaInicioTimbrado() != null) {
            // Se asume que emisor.getFechaInicioTimbrado() ya devuelve un String "YYYY-MM-DD" o un LocalDate
            json.put("dFeIniT", emisor.getFechaInicioTimbrado().toString()  );
        }



        // --- Datos del Emisor SIFEN ---

        if (!json.has("dRucEm")) {
            json.put("dRucEm", Long.parseLong(emisor.getRuc()));
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
                json.put("cDepEmi", emisor.getDepartamento().getId());
            }
            if (!json.has("dDesDepEmi")) {
                json.put("dDesDepEmi", emisor.getDepartamento().getDescripcion());
            }
        }

        // Ubicación (Distrito)
        if (emisor.getDistrito() != null) {
            if (!json.has("cDisEmi")) {
                json.put("cDisEmi", emisor.getDistrito().getId());
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
}