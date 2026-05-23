package org.sifenboot.worker.scheduler;

import org.sifenboot.app.admin.emisor.model.Emisor;
import org.sifenboot.app.admin.emisor.repository.EmisorRepository;
import org.sifenboot.worker.service.SifenTransmissionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SifenTaskScheduler {

    private final EmisorRepository emisorRepository;
    private final SifenTransmissionService transmissionService;

    // Mantiene el estado en memoria durante toda la vida de la app
    private Integer ultimoEmisorIdProcesado = 0;

    public SifenTaskScheduler(EmisorRepository emisorRepository, SifenTransmissionService transmissionService) {
        this.emisorRepository = emisorRepository;
        this.transmissionService = transmissionService;
    }

    /**
     * Corre Estrictamente UN Emisor por turno.
     * Al terminar este único emisor, el método muere y Spring espera 10 segundos quietos.
     */
    @Scheduled(fixedDelayString = "#{T(org.springframework.boot.convert.DurationStyle).detect('${sifen.worker.delay}').parse('${sifen.worker.delay}').toMillis()}")
    public void executePendingTransmissions() {
        System.out.println("[Scheduler] --- Iniciando Turno Único ---");

        // 1. Condición 'Mayor Qué': Trae solo el siguiente de la lista (LIMIT 1)
        Optional<Emisor> emisorOpt = emisorRepository.findFirstByIdGreaterThanOrderByIdAsc(ultimoEmisorIdProcesado);

        // 2. Si ya no hay más adelante, reiniciamos para el PRÓXIMO turno
        if (emisorOpt.isEmpty()) {
            System.out.println("[Scheduler] Llegamos al final de la lista. Reiniciando ciclo al primer emisor.");
            emisorOpt = emisorRepository.findFirstByOrderByIdAsc();
        }

        // 3. Procesamos el único emisor seleccionado para este turno
        if (emisorOpt.isPresent()) {
            Emisor emisorActual = emisorOpt.get();

            // Avanzamos el puntero en memoria
            this.ultimoEmisorIdProcesado = emisorActual.getId();

            System.out.println("[Scheduler] Procesando solo Emisor ID: " + emisorActual.getId() + " [" + emisorActual.getCodEmisor() + "]");

            try {
                // Ejecución sincrónica del emisor del turno
                this.transmissionService.transmit(emisorActual);
            } catch (Exception e) {
                System.err.println("[Scheduler] Error en turno: " + e.getMessage());
            }
        } else {
            System.out.println("[Scheduler] Sin emisores en la base de datos.");
        }

        System.out.println("[Scheduler] --- Fin del Turno. Liberando hilo ---");
    }
}