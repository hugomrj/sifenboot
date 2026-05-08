package org.sifenboot.app.documento.repository;

import org.sifenboot.app.documento.model.EstadoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstadoDocumentoRepository extends JpaRepository<EstadoDocumento, Short> {

    Optional<EstadoDocumento> findByCodigo(String codigo);

}