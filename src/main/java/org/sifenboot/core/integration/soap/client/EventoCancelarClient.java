package org.sifenboot.core.integration.soap.client;

import org.sifenboot.core.integration.sifen.ServerSifen;
import org.sifenboot.core.integration.sifen.config.SifenProperties_Deprecated;
import org.sifenboot.core.integration.soap.config.SSLConfig;
import org.sifenboot.core.integration.soap.request.EventoCancelarSoapRequest;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class EventoCancelarClient {

    private final EventoCancelarSoapRequest eventoCancelarRequest;
    private final SSLConfig sslConfig;
    private final ServerSifen serverSifen;
    private final SifenProperties_Deprecated sifenPropertiesDeprecated;

    private HttpClient httpClient;

    @Autowired
    public EventoCancelarClient(EventoCancelarSoapRequest eventoCancelarRequest,
                                SSLConfig sslConfig,
                                ServerSifen serverSifen,
                                SifenProperties_Deprecated sifenPropertiesDeprecated) {
        this.eventoCancelarRequest = eventoCancelarRequest;
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

    public HttpResponse<String> cancelarEvento(String id, String mOtEve) {
        try {

            String endpointUrl = buildCancelarEventoUrl();

            String xmlRequest = eventoCancelarRequest.createCancelarXml(id, mOtEve);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpointUrl))
                    .header("Content-Type", "application/soap+xml;charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(xmlRequest))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString()
            );

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error cancelando evento", e);
        }
    }



    private String buildCancelarEventoUrl() {
        String environment = sifenPropertiesDeprecated.getAmbiente();
        String baseUrl = serverSifen.getServer(environment);
        return baseUrl + "/de/ws/eventos/evento.wsdl";

    }
}

