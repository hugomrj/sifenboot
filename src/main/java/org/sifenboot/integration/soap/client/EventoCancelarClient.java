package org.sifenboot.integration.soap.client;

import org.sifenboot.integration.sifen.ServerSifen;
import org.sifenboot.integration.sifen.config.SifenProperties;
import org.sifenboot.integration.soap.config.SSLConfig;
import org.sifenboot.integration.soap.request.EventoCancelarSoapRequest;
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
    private final SifenProperties sifenProperties;

    private HttpClient httpClient;

    @Autowired
    public EventoCancelarClient(EventoCancelarSoapRequest eventoCancelarRequest,
                                SSLConfig sslConfig,
                                ServerSifen serverSifen,
                                SifenProperties sifenProperties) {
        this.eventoCancelarRequest = eventoCancelarRequest;
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
        String environment = sifenProperties.getAmbiente();
        String baseUrl = serverSifen.getServer(environment);
        return baseUrl + "/de/ws/eventos/evento.wsdl";

    }
}

