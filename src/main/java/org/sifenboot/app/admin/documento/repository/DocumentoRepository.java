package org.sifenboot.app.admin.documento.repository;

import org.sifenboot.app.admin.documento.model.Documento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    /**
     * Busca un documento específico por ID incluyendo su historial de respuestas
     */
    @Query("""
        SELECT d
        FROM Documento d
        LEFT JOIN FETCH d.respuestas
        WHERE d.id = :id
    """)
    Optional<Documento> findByIdWithRespuestas(
            @Param("id") Long id
    );

    /**
     * Consulta optimizada:
     * Trae documentos junto con respuestas
     */
    @Query("""
        SELECT DISTINCT d
        FROM Documento d
        LEFT JOIN FETCH d.respuestas
        ORDER BY
            d.establecimiento DESC,
            d.puntoExpedicion DESC,
            d.numeroDocumento DESC
    """)
    List<Documento> findAllWithRespuestas();

    /**
     * Documentos pendientes para envío a SIFEN
     */
    List<Documento>
    findTop50ByEstadoIdOrderByFechaCreacionAsc(
            Short estadoId
    );

}