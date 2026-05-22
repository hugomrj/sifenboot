package org.sifenboot.worker.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class SifenTransmissionService {

    /**
     * Procesa y transmite el documento de manera asíncrona hacia el SIFEN.
     * Al estar anotado con @Async, se ejecuta en un hilo separado del pool de Spring.
     *
     * @param documentId Identificador del documento a procesar.
     */
    @Async
    public void transmit(Long documentId) {
        String currentThread = Thread.currentThread().getName();
        System.out.println("[" + currentThread + "] -> Iniciando ciclo de transmisión para Documento ID: " + documentId);

        try {
            // TODO: 1. Cambiar estado local a "PROCESANDO" (Evita colisiones)
            // TODO: 2. Generar la estructura del DE (Documento Electrónico)
            // TODO: 3. Firmar digitalmente el XML usando el certificado PKCS#12

            System.out.println("[" + currentThread + "] -> Conectando con los Web Services del SIFEN (SET)...");
            // Simulamos la latencia de red de la SET y el procesamiento de la firma
            Thread.sleep(3000);

            // TODO: 4. Procesar el resultado del CDC (Aprobado / Rechazado)
            System.out.println("[" + currentThread + "] -> ¡Éxito! Documento ID " + documentId + " transmitido y procesado correctamente.");

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("[" + currentThread + "] -> Error: El hilo de transmisión fue interrumpido.");
            // TODO: Revertir estado del documento a PENDIENTE para reintento
        } catch (Exception e) {
            System.err.println("[" + currentThread + "] -> Error en la transmisión del Documento ID: " + documentId + ". Motivo: " + e.getMessage());
            // TODO: Manejar el reintento devolviendo el estado a PENDIENTE si fue un timeout de la SET
        }
    }
}