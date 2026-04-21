package org.sifenboot.shell.repository;

import org.sifenboot.identity.domain.repository.Certificado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CertificadoRepository extends JpaRepository<Certificado, Long> {
    // Para cargar los datos del certificado de un emisor
    Optional<Certificado> findByEmisorId(Long emisorId);

    // Para saber si ya tiene uno antes de intentar guardar
    boolean existsByEmisorId(Long emisorId);

    // Borrar el certificado de un emisor específico
    void deleteByEmisorId(Long emisorId);
}