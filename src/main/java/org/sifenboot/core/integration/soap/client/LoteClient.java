package org.sifenboot.core.integration.soap.client;

import org.sifenboot.core.sifen.ServerSifen;
import org.sifenboot.core.integration.sifen.config.SifenProperties_Deprecated;
import org.sifenboot.security.certificado.model.Certificado;
import org.sifenboot.security.config.identity.SSLConfig;
import org.sifenboot.core.integration.soap.request.LoteConsultaSoapRequest;
import org.sifenboot.core.integration.soap.request.LoteRecibeSoapRequest;
import org.sifenboot.core.integration.util.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

@Service
public class LoteClient {

    private final LoteConsultaSoapRequest loteConsultaRequest;
    private final LoteRecibeSoapRequest loteRecibeRequest;
    private final SSLConfig sslConfig;
    private final ServerSifen serverSifen;

    private HttpClient httpClient;

    @Autowired
    public LoteClient(LoteConsultaSoapRequest loteConsultaRequest,
                      LoteRecibeSoapRequest loteRecibeRequest,
                      SSLConfig sslConfig,
                      ServerSifen serverSifen
                      ) {
        this.loteConsultaRequest = loteConsultaRequest;
        this.loteRecibeRequest = loteRecibeRequest;
        this.sslConfig = sslConfig;
        this.serverSifen = serverSifen;
    }

    public HttpResponse<String> consultaLote(String lote, Certificado certificado) {
        try {

            SSLContext sslContext = sslConfig.createSSLContext(
                    certificado.getP12Contenido(),
                    certificado.getP12Password()
            );

            HttpClient httpClient = HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .build();

            String ambiente = certificado.getEmisor().getConfiguracion().getAmbiente();
            String endpointUrl = buildConsultaUrl(ambiente);
            String xmlRequest = loteConsultaRequest.createQueryXml(lote);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpointUrl))
                    .header("Content-Type", "application/soap+xml;charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(xmlRequest))
                    .build();

            return httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString()
            );

        } catch (Exception e) {
            throw new RuntimeException("Error consultando lote", e);
        }
    }

    public HttpResponse<String> recibeLote(String xmlFactura, Certificado certificado) {
        try {
            xmlFactura = "<rLoteDE>" + xmlFactura + "</rLoteDE>";

            byte[] zipData = IOUtils.compressXmlToZip(xmlFactura);
            String base64Zip = Base64.getEncoder().encodeToString(zipData);

            String ambiente = certificado.getEmisor().getConfiguracion().getAmbiente();
            String endpointUrl = buildRecepcionUrl(ambiente);
            String xmlRequest = loteRecibeRequest.createEnvioXml(base64Zip);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpointUrl))
                    .header("Content-Type", "application/soap+xml;charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(xmlRequest))
                    .build();

            return httpClient.send(
                request, HttpResponse.BodyHandlers.ofString()
            );

        } catch (Exception e) {
            throw new RuntimeException("Error enviando lote", e);
        }
    }


    private String buildConsultaUrl(String ambiente) {
        String baseUrl = serverSifen.getServer(ambiente);
        return baseUrl + "/de/ws/consultas/consulta-lote.wsdl";
    }

    private String buildRecepcionUrl(String ambiente) {
        String baseUrl = serverSifen.getServer(ambiente);
        return baseUrl + "/de/ws/async/recibe-lote.wsdl";
    }
}
