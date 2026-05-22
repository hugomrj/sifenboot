package org.sifenboot.worker.scheduler;

import org.sifenboot.worker.service.SifenTransmissionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SifenTaskScheduler {

    private final SifenTransmissionService transmissionService;

    // Inyección por constructor limpia
    public SifenTaskScheduler(SifenTransmissionService transmissionService) {
        this.transmissionService = transmissionService;
    }

    /**
     * Escanea de forma periódica los documentos pendientes.
     * fixedDelay asegura que el contador de 10 segundos empiece a correr
     * RECIÉN cuando esta iteración de escaneo termine de ejecutarse.
     */
    @Scheduled(fixedDelay = 10000)
    public void executePendingTransmissions() {
        System.out.println("[Scheduler] Buscando documentos en estado PENDIENTE...");

        // ID de prueba que simula haber sido recuperado de la base de datos
        Long documentIdDemo = 5501L;

        System.out.println("[Scheduler] Documento encontrado (" + documentIdDemo + "). Despachando transmisión...");

        // Se envía al servicio asíncrono. El Scheduler no se traba esperando la respuesta.
        this.transmissionService.transmit(documentIdDemo);
    }
}