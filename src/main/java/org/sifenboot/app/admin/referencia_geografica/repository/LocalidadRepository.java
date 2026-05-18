package org.sifenboot.app.admin.referencia_geografica.repository;

import org.sifenboot.app.admin.referencia_geografica.model.Localidad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LocalidadRepository extends JpaRepository<Localidad, Integer> {
    // Filtra localidades por el ID del distrito
    List<Localidad> findByDistritoId(Integer distritoId);
}