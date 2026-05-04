package org.sifenboot.app.documento.repository;


import org.sifenboot.app.documento.model.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    /**
     * Consulta optimizada: Trae documentos junto con sus respuestas
     * en una sola llamada (evita el problema de LazyInitializationException)
     */
    @Query("SELECT DISTINCT d FROM Documento d LEFT JOIN FETCH d.respuestas ORDER BY d.fechaEmision DESC")
    List<Documento> findAllWithRespuestas();

    /**
     * Busca un documento específico por ID incluyendo su historial de respuestas
     */
    @Query("SELECT d FROM Documento d LEFT JOIN FETCH d.respuestas WHERE d.id = :id")
    Optional<Documento> findByIdWithRespuestas(@Param("id") Long id);
}