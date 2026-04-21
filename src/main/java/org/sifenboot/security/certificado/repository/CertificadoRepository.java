package org.sifenboot.security.certificado.repository;

import org.sifenboot.security.certificado.model.Certificado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CertificadoRepository extends JpaRepository<Certificado, Long> {

    // Busca por la PK (id) de la entidad Emisor
    Optional<Certificado> findByEmisorId(Long emisorId);

    // Corregido: Spring navegará Certificado -> Emisor -> codEmisor
    Optional<Certificado> findByEmisorCodEmisor(String codEmisor);

    // Borrar el certificado de un emisor específico por su ID
    void deleteByEmisorId(Long emisorId);
}


