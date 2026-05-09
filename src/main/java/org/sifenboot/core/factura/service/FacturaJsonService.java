package org.sifenboot.core.factura.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.sifenboot.app.emisor.model.Emisor;
import org.sifenboot.app.emisor.service.EmisorService;
import org.springframework.stereotype.Service;

@Service
public class FacturaJsonService {

    private final EmisorService emisorService;

    public FacturaJsonService(EmisorService emisorService) {
        this.emisorService = emisorService;
    }

    public JsonNode completar(String codEmisor, JsonNode facturaInput) {

        // buscar emisor
        Emisor emisor = emisorService.findByCodEmisor(codEmisor);

        // clonar json
        ObjectNode json = facturaInput.deepCopy();

        // completar datos
        json.put("rucEmisor", emisor.getRuc());
        json.put("razonSocialEmisor", emisor.getRazonSocial());

        return json;
    }
}