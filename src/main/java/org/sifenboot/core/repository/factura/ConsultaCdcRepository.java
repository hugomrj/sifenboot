package org.sifenboot.core.repository.factura;

import org.sifenboot.core.integration.soap.client.DEClient;
import org.sifenboot.security.certificado.model.Certificado;
import org.springframework.stereotype.Repository;
import java.net.http.HttpResponse;


@Repository
public class ConsultaCdcRepository {

    private final DEClient client;

    public ConsultaCdcRepository(DEClient client) {
        this.client = client;
    }

    public String buscarPorCdc(String cdc,  Certificado certificado) {
        try {
            HttpResponse<String> httpResponse = client.consultaDE(cdc, certificado);
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