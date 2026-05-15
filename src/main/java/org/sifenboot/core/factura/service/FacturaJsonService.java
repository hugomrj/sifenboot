package org.sifenboot.core.factura.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.sifenboot.app.admin.emisor.model.Emisor;
import org.sifenboot.app.admin.emisor.service.EmisorService;
import org.sifenboot.core.integration.builder.CodigoSeguridadGenerator;
import org.springframework.stereotype.Service;


@Service
public class FacturaJsonService {

    private final EmisorService emisorService;
    private final CodigoSeguridadGenerator codigoSeguridadGenerator;

    public FacturaJsonService(
            EmisorService emisorService,
            CodigoSeguridadGenerator codigoSeguridadGenerator
    ) {
        this.emisorService = emisorService;
        this.codigoSeguridadGenerator = codigoSeguridadGenerator;
    }

    public JsonNode completar(String codEmisor, JsonNode facturaInput) {

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

        // NOTA: Como 'Distrito' y 'Ciudad' no están explícitos en tu modelo 'Emisor',
        // te dejo los bloques listos por si la info viene en el input o si debés hardcodearlos temporalmente.
        if (!json.has("cDisEmi")) {
            // json.put("cDisEmi", ...);
        }
        if (!json.has("dDesDisEmi")) {
            // json.put("dDesDisEmi", ...);
        }
        if (!json.has("cCiuEmi")) {
            // json.put("cCiuEmi", ...);
        }
        if (!json.has("dDesCiuEmi")) {
            // json.put("dDesCiuEmi", ...);
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