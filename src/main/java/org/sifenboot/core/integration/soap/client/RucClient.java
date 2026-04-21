package org.sifenboot.core.integration.soap.client;


import org.sifenboot.core.integration.sifen.ServerSifen;
import org.sifenboot.core.integration.sifen.config.SifenProperties_Deprecated;
import org.sifenboot.security.config.identity.SSLConfig;
import org.sifenboot.core.integration.soap.request.RucConsultaSoapRequest;
import org.sifenboot.security.certificado.model.Certificado;
import org.springframework.stereotype.Service;


import javax.net.ssl.SSLContext;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class  RucClient {

    private final RucConsultaSoapRequest rucRequest;
    private final SSLConfig sslConfig;
    private final ServerSifen serverSifen;

    private HttpClient httpClient;

    public RucClient(
            RucConsultaSoapRequest rucRequest,
            SSLConfig sslConfig,
            ServerSifen serverSifen
    ) {
        this.rucRequest = rucRequest;
        this.sslConfig = sslConfig;
        this.serverSifen = serverSifen;
    }

    public HttpResponse<String> consultaRUC(String ruc, Certificado certificado) {
        try {

            SSLContext sslContext = sslConfig.createSSLContext(
                    certificado.getP12Contenido(),
                    certificado.getP12Password()
            );

            HttpClient httpClient = HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .build();

            String ambiente = certificado.getEmisor().getConfiguracion().getAmbiente();
            String endpointUrl = buildEndpointUrl(ambiente);
            String xmlRequest = rucRequest.createQueryXml(ruc);

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
            throw new RuntimeException("Error consultando RUC con certificado de: " +
                    certificado.getEmisor().getRazonSocial() + ": " + e.getMessage(), e);
        }
    }




    private String buildEndpointUrl(String ambiente) {
        String baseUrl = serverSifen.getServer(ambiente);
        return baseUrl + "/de/ws/consultas/consulta-ruc.wsdl";
    }


}
