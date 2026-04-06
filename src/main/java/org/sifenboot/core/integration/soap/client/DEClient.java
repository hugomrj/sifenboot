package org.sifenboot.core.integration.soap.client;

import org.sifenboot.core.integration.sifen.ServerSifen;
import org.sifenboot.core.integration.sifen.config.SifenProperties_Deprecated;
import org.sifenboot.core.integration.soap.config.SSLConfig;
import org.sifenboot.core.integration.soap.request.DeConsultaSoapRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class DEClient {

    private final DeConsultaSoapRequest deRequest;
    private final SSLConfig sslConfig;
    private final ServerSifen serverSifen;
    private final SifenProperties_Deprecated sifenPropertiesDeprecated;

    private HttpClient httpClient;

    public DEClient(
            DeConsultaSoapRequest deRequest,
            SSLConfig sslConfig,
            ServerSifen serverSifen,
            SifenProperties_Deprecated sifenPropertiesDeprecated
    ) {
        this.deRequest = deRequest;
        this.sslConfig = sslConfig;
        this.serverSifen = serverSifen;
        this.sifenPropertiesDeprecated = sifenPropertiesDeprecated;
    }
/*
    @PostConstruct
    void initialize() {
        this.httpClient = HttpClient.newBuilder()
                .sslContext(sslConfig.createSSLContext())
                .build();
    }
*/
    public HttpResponse<String> consultaDE(String cdc) {
        try {
            String endpointUrl = buildEndpointUrl();
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

    private String buildEndpointUrl() {
        String environment = sifenPropertiesDeprecated.getAmbiente();
        String baseUrl = serverSifen.getServer(environment);
        return baseUrl + "/de/ws/consultas/consulta.wsdl";
    }
}

