package org.sifenboot.worker.service;

import org.sifenboot.app.admin.emisor.model.Emisor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class SifenTransmissionService {

    /**
     * Procesa y transmite los lotes de documentos pendientes del emisor asignado.
     * SIN @Async: Corre en el mismo hilo del Scheduler garantizando un único proceso a la vez.
     *
     * @param emisor Entidad que representa la empresa y el esquema a procesar.
     */
    public void transmit(Emisor emisor) {
        String currentThread = Thread.currentThread().getName();
        String esquema = emisor.getCodEmisor(); // Ej: "sanisidro"

        System.out.println("[" + currentThread + "] >>> Iniciando procesamiento para el esquema: " + esquema);

        try {
            // TODO: 1. Cambiar el search_path de la conexión actual a 'esquema'
            //          Ej: SET search_path TO sanisidro, public;

            // TODO: 2. Buscar si quedaron lotes colgados en estado 2 (En Proceso) para consultar su resultado

            // TODO: 3. Reservar hasta 50 documentos (estado_id = 1, numero_lote IS NULL)
            //          asignándoles un nuevo número de lote y cambiando su estado local a 2 (En Proceso)

            // TODO: 4. Generar la estructura del DE, firmar XML (PKCS#12) y empaquetar el lote

            System.out.println("[" + currentThread + "] -> Conectando con los Web Services del SIFEN para el esquema [" + esquema + "]...");
            // Simulamos la latencia de la red de la SET y procesamiento local
            Thread.sleep(3000);

            // TODO: 5. Procesar respuesta del lote (Pasar documentos a Aprobados o Rechazados)
            System.out.println("[" + currentThread + "] -> Turno completado con éxito para el esquema: " + esquema);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("[" + currentThread + "] -> Error: El hilo del Worker fue interrumpido.");
        } catch (Exception e) {
            System.err.println("[" + currentThread + "] -> Error en el procesamiento del esquema: " + esquema + ". Motivo: " + e.getMessage());
        }
    }
}