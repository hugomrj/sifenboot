package org.sifenboot.core.controller.sifen;

import org.sifenboot.core.integration.sifen.ServerSifen;
import org.sifenboot.security.config.identity.SSLConfig;
import org.sifenboot.core.integration.soap.request.LoteConsultaSoapRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping("/api/sifen")
public class SifenHealthController {

    private final LoteConsultaSoapRequest loteRequest;
    private final ServerSifen serverSifen;
    private final SSLConfig sslConfig;

    // Nombre del archivo de log
    private static final String LOG_FILE_NAME = "sifen_status_logs.txt";

    public SifenHealthController(LoteConsultaSoapRequest loteRequest,
                                 ServerSifen serverSifen, SSLConfig sslConfig) {
        this.loteRequest = loteRequest;
        this.serverSifen = serverSifen;
        this.sslConfig = sslConfig;
    }

    @GetMapping("/status/{env}")
    public ResponseEntity<?> checkStatus(@PathVariable String env) {
        long startTime = System.currentTimeMillis();
        long targetDuration = 6000;

        // Generamos el lote de 20 dígitos empezando con 1
        String randomLote = "1" + generateRandomDigits(19);

        try {
            String baseUrl = serverSifen.getServer(env.toLowerCase());
            String endpointUrl = baseUrl + "/de/ws/consultas/consulta-lote.wsdl";
            String xmlRequest = loteRequest.createQueryXml(randomLote);

            HttpClient client = HttpClient.newBuilder()
                    .sslContext(sslConfig.createSSLContext())
                    .connectTimeout(java.time.Duration.ofSeconds(10))
                    .build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endpointUrl))
                    .header("Content-Type", "text/xml; charset=UTF-8")
                    .POST(HttpRequest.BodyPublishers.ofString(xmlRequest))
                    .build();

            // 1. Ejecución de la petición SOAP
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int statusCode = response.statusCode();
            String responseBody = response.body();

            // Logueo en consola para debug
            System.out.println("Lote: " + randomLote + " | Status: " + statusCode);

            // 2. Persistencia en archivo .txt
            saveToLogFile(randomLote, statusCode, env);

            long actualExecutionTime = System.currentTimeMillis() - startTime;
            boolean sifenOk = (statusCode == 200 && responseBody.contains("rResEnviConsLoteDe"));

            // 3. PAUSA COMPENSATORIA (Target 6s)
            long sleepTime = targetDuration - actualExecutionTime;
            if (sleepTime > 0) {
                try { Thread.sleep(sleepTime); } catch (InterruptedException ignored) {}
            }

            long totalDuration = System.currentTimeMillis() - startTime;

            return ResponseEntity.ok(Map.of(
                    "lote", randomLote,
                    "status", sifenOk ? "ONLINE" : "OFFLINE",
                    "ambiente", env.toUpperCase(),
                    "httpCode", statusCode,
                    "ms", actualExecutionTime,
                    "totalMs", totalDuration,
                    "timestamp", LocalDateTime.now().toString()
            ));

        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "lote", randomLote,
                    "error", e.getMessage(),
                    "timestamp", LocalDateTime.now().toString()
            ));
        }
    }

    /**
     * Guarda el log en un archivo .txt. Si no existe lo crea, si existe añade al final.
     */
    private void saveToLogFile(String lote, int status, String ambiente) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String logEntry = String.format("%s | Lote: %s | HTTP: %d | Env: %s%n",
                    timestamp, lote, status, ambiente.toUpperCase());

            Path path = Paths.get(LOG_FILE_NAME);

            Files.write(path, logEntry.getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (Exception e) {
            System.err.println("No se pudo escribir en el log: " + e.getMessage());
        }
    }

    /**
     * Genera una cadena de n dígitos aleatorios
     */
    private String generateRandomDigits(int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(ThreadLocalRandom.current().nextInt(10));
        }
        return sb.toString();
    }
}