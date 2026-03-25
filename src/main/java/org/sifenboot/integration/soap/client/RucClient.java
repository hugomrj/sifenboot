package org.sifenboot.integration.soap.client;


import org.sifenboot.integration.sifen.ServerSifen;
import org.sifenboot.integration.sifen.config.SifenProperties;
import org.sifenboot.integration.soap.config.SSLConfig;
import org.sifenboot.integration.soap.request.RucConsultaSoapRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;


import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class  RucClient {

    private final RucConsultaSoapRequest rucRequest;
    private final SSLConfig sslConfig;
    private final ServerSifen serverSifen;
    private final SifenProperties sifenProperties;

    private HttpClient httpClient;

    public RucClient(
            RucConsultaSoapRequest rucRequest,
            SSLConfig sslConfig,
            ServerSifen serverSifen,
            SifenProperties sifenProperties
    ) {
        this.rucRequest = rucRequest;
        this.sslConfig = sslConfig;
        this.serverSifen = serverSifen;
        this.sifenProperties = sifenProperties;
    }

    @PostConstruct
    void initialize() {
        this.httpClient = HttpClient.newBuilder()
                .sslContext(sslConfig.createSSLContext())
                .build();
    }

    public HttpResponse<String> consultaRUC(String ruc) {
        try {
            String endpointUrl = buildEndpointUrl();
            String xmlRequest = rucRequest.createQueryXml(ruc);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpointUrl))
                    //.header("Content-Type", "application/soap+xml;charset=UTF-8")
                    .header("Content-Type", "text/xml; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(xmlRequest))
                    .build();

            return httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            throw new RuntimeException("Failed to query RUC: " + e.getMessage(), e);
        }
    }

    private String buildEndpointUrl() {
        String environment = sifenProperties.getAmbiente();
        String baseUrl = serverSifen.getServer(environment);
        return baseUrl + "/de/ws/consultas/consulta-ruc.wsdl";
    }
}
