package org.sifenboot.worker.service;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.transaction.Transactional;
import org.sifenboot.app.admin.emisor.model.Emisor;
import org.sifenboot.app.admin.emisor.service.EmisorService;
import org.sifenboot.core.lote.service.LoteEnvioService;
import org.sifenboot.security.certificado.model.Certificado;
import org.sifenboot.security.certificado.service.CertificadoService;
import org.sifenboot.setup.db.DbUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;



@Service
public class SifenTransmissionService {

    private final DbUtils db;
    private final EmisorService emisorService;
    private final CertificadoService certificadoService;
    private final LoteEnvioService loteEnvioService;

    public SifenTransmissionService(
            DbUtils db,
            EmisorService emisorService,
            CertificadoService certificadoService,
            LoteEnvioService loteEnvioService
    ) {
        this.db = db;
        this.emisorService = emisorService;
        this.certificadoService = certificadoService;
        this.loteEnvioService = loteEnvioService;
    }

    @Transactional
    public void transmit(Emisor emisor) {

        String esquema = emisor.getCodEmisor();

        try {

            // 1. Schema global
            db.setSchema("public");

            // 2. Obtener certificado activo
            Certificado certificado =
                    certificadoService.getActiveCertificateByEmisorCode(esquema);

            // 3. Cambiar al schema del emisor
            db.setSchema(esquema);

            // 4. Procesar pendientes
            loteEnvioService.procesarPendientes(
                    emisor,
                    certificado
            );

        } catch (Exception e) {

            System.err.println(
                    "[Worker] Error esquema "
                            + esquema
            );
            e.printStackTrace();


        }
    }
}