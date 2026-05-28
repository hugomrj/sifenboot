package org.sifenboot.app.admin.documento.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.sifenboot.app.admin.documento.model.Documento;
import org.sifenboot.app.admin.documento.repository.DocumentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class DocumentoService {

    @PersistenceContext
    private EntityManager entityManager;
    private final DocumentoRepository documentoRepository;

    public DocumentoService(DocumentoRepository documentoRepository) {
        this.documentoRepository = documentoRepository;
    }


    @Transactional(readOnly = true)
    public void setSchemaContext(String codEmisor) {
        String schema = codEmisor.replaceAll("[^a-zA-Z0-9_]", "");
        entityManager.createNativeQuery("SET search_path TO " + schema).executeUpdate();
    }


    @Transactional(readOnly = true)
    public List<Documento> findAll() {
        return documentoRepository.findAllWithRespuestas();
    }


}