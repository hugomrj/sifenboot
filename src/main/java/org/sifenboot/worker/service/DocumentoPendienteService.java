package org.sifenboot.worker.service;


import org.sifenboot.app.admin.emisor.model.Emisor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentoPendienteService {

    public List<String> buscarPendientes(
            Emisor emisor
    ) {

        System.out.println(
                "[Pendientes] Buscando pendientes para "
                        + emisor.getCodEmisor()
        );

        List<String> documentos = new ArrayList<>();

        documentos.add("DOC-1");
        documentos.add("DOC-2");
        documentos.add("DOC-3");

        return documentos;
    }
}