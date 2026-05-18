package org.sifenboot.app.admin.referencia_geografica.repository;

import org.sifenboot.app.admin.referencia_geografica.model.Distrito;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DistritoRepository extends JpaRepository<Distrito, Integer> {
    // Filtra distritos por el ID del departamento
    List<Distrito> findByDepartamentoId(Integer departamentoId);
}
