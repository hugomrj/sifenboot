package com.sifen.api.repository.factura;

import com.sifen.api.integration.soap.client.DEClient;
import org.springframework.stereotype.Repository;
import java.net.http.HttpResponse;


@Repository
public class ConsultaCdcRepository {

    private final DEClient deClient;

    public ConsultaCdcRepository(DEClient deClient) {
        this.deClient = deClient;
    }

    public String buscarPorCdc(String cdc) {
        try {
            HttpResponse<String> httpResponse = deClient.consultaDE(cdc);
            String xml = httpResponse.body();

            // NO decodificar entidades aquí - dejar que el servicio lo maneje
            // Solo reemplazar &lt; y &gt; básicos si es necesario
            xml = xml.replace("&lt;", "<").replace("&gt;", ">");

            return xml;

        } catch (Exception e) {
            return "<error>" +
                    "<mensaje>Error consultando CDC: " + e.getMessage() + "</mensaje>" +
                    "</error>";
        }
    }
}