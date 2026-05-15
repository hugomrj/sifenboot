package org.sifenboot.app.admin.emisor.repository;

import org.sifenboot.app.admin.emisor.model.Emisor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface EmisorRepository extends JpaRepository<Emisor, Long> {
    Optional<Emisor> findByCodEmisor(String codEmisor);
}
