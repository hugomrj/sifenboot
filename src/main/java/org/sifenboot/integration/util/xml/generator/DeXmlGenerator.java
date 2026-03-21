package org.sifenboot.integration.util.xml.generator;

import com.fasterxml.jackson.databind.JsonNode;
import org.sifenboot.integration.builder.CdcBuilder;
import org.sifenboot.integration.builder.DeXmlBuilder;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Component;
import org.sifenboot.integration.de.DeComplemento;

@Component
public class DeXmlGenerator {

    private final DeComplemento complegen;
    private final ObjectProvider<DeXmlBuilder> deXmlBuilderProvider;

    public DeXmlGenerator(
            DeComplemento complegen,
            ObjectProvider<DeXmlBuilder> deXmlBuilderProvider
    ) {
        this.complegen = complegen;
        this.deXmlBuilderProvider = deXmlBuilderProvider;
    }

    public String generar(JsonNode facturaJson) throws Exception {

        String json = facturaJson.toString().trim();

        String cdc = CdcBuilder.obtenerCDC(json);

        String jsonCom = complegen.getJsonCom(json, cdc);

        String jsonAll = jsonCom.substring(0, jsonCom.length() - 1)
                + ", "
                + json.substring(1);

        return deXmlBuilderProvider
                .getObject()
                .generateXml(jsonAll, cdc);
    }
}
