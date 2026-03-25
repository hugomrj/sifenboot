package org.sifenboot.controller.ruc;

import org.sifenboot.integration.sifen.ServerSifen;
import org.sifenboot.integration.soap.config.SSLConfig;
import org.sifenboot.integration.soap.request.RucConsultaSoapRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@RestController
@RequestMapping("/consulta/ruc/debug")
public class SifenStatusController {

    private final RucConsultaSoapRequest rucRequest;
    private final ServerSifen serverSifen;
    private final SSLConfig sslConfig;

    public SifenStatusController(RucConsultaSoapRequest rucRequest, ServerSifen serverSifen, SSLConfig sslConfig) {
        this.rucRequest = rucRequest;
        this.serverSifen = serverSifen;
        this.sslConfig = sslConfig;
    }

    @GetMapping("/{env}")
    public ResponseEntity<?> ejecutarDiagnostico(@PathVariable String env) {

        // 1. Retraso controlado
        try { Thread.sleep(1000); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }

        long start = System.currentTimeMillis();

        try {
            String baseUrl = serverSifen.getServer(env.toLowerCase());
            String endpointUrl = baseUrl + "/de/ws/consultas/consulta-ruc.wsdl";
            String xmlRequest = rucRequest.createQueryXml("80089752");

            HttpClient client = HttpClient.newBuilder()
                    .sslContext(sslConfig.createSSLContext())
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpointUrl))
                    .header("Content-Type", "text/xml; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(xmlRequest))
                    .build();

            // Ejecución
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // CORRECCIÓN: Definir variable antes de usarla
            String responseBody = response.body();
            long duration = System.currentTimeMillis() - start;
            boolean sifenOk = responseBody.contains("rResEnviConsRUC");

            // CORRECCIÓN: Definir timestamp solo una vez
            String ts = java.time.LocalDateTime.now().toString();

            // Impresión en consola
            System.out.println("\n--- INICIO XML RECIBIDO ---");
            System.out.println(responseBody);
            System.out.println("--- FIN XML RECIBIDO ---");

            String logEntry = String.format("[%s] ENV: %s | HTTP: %d | TIME: %dms | SIFEN_OK: %b",
                    ts, env.toUpperCase(), response.statusCode(), duration, sifenOk);

            System.out.println(logEntry);

            return ResponseEntity.ok(Map.of(
                    "ambiente", env.toUpperCase(),
                    "httpCode", response.statusCode(),
                    "ms", duration,
                    "sifenOk", sifenOk,
                    "xml", responseBody,
                    "log", logEntry
            ));

        } catch (Exception e) {
            System.err.println("[ERROR] " + java.time.LocalDateTime.now() + " | MSG: " + e.getMessage());
            return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
        }
    }

}