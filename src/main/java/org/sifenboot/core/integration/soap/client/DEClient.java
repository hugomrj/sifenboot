package org.sifenboot.core.integration.soap.client;

import org.sifenboot.core.sifen.ServerSifen;
import org.sifenboot.core.integration.sifen.config.SifenProperties_Deprecated;
import org.sifenboot.security.certificado.model.Certificado;
import org.sifenboot.security.config.identity.SSLConfig;
import org.sifenboot.core.integration.soap.request.DeConsultaSoapRequest;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class DEClient {

    private final DeConsultaSoapRequest deRequest;
    private final SSLConfig sslConfig;
    private final ServerSifen serverSifen;

    private HttpClient httpClient;

    public DEClient(
            DeConsultaSoapRequest deRequest,
            SSLConfig sslConfig,
            ServerSifen serverSifen
    ) {
        this.deRequest = deRequest;
        this.sslConfig = sslConfig;
        this.serverSifen = serverSifen;
    }

    public HttpResponse<String> consultaDE(String cdc, Certificado certificado) {
        try {

            SSLContext sslContext = sslConfig.createSSLContext(
                    certificado.getP12Contenido(),
                    certificado.getP12Password()
            );

            HttpClient httpClient = HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .build();

            String ambiente = certificado.getEmisor().getConfiguracion().getAmbiente();
            String endpointUrl = buildEndpointUrl(ambiente );
            String xmlRequest = deRequest.createQueryXml(cdc);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpointUrl))
                    .header("Content-Type", "text/xml; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(xmlRequest))
                    .build();

            return httpClient.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to query DE: " + e.getMessage(), e
            );
        }
    }

    private String buildEndpointUrl(String ambiente) {
        String baseUrl = serverSifen.getServer(ambiente);
        return baseUrl + "/de/ws/consultas/consulta.wsdl";
    }

}

