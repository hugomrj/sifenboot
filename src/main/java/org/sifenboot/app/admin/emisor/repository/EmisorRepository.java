package org.sifenboot.app.admin.emisor.repository;

import org.sifenboot.app.admin.emisor.model.Emisor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;



@Repository
public interface EmisorRepository extends JpaRepository<Emisor, Integer> {

    // Método original
    Optional<Emisor> findByCodEmisor(String codEmisor);

    // NUEVO: Método optimizado para traer datos geográficos (soluciona el error de la lista)
    @Query("SELECT e FROM Emisor e " +
            "LEFT JOIN FETCH e.departamento " +
            "LEFT JOIN FETCH e.distrito " +
            "LEFT JOIN FETCH e.localidad")
    List<Emisor> findAllWithGeoData();



    // NUEVO: Método optimizado para traer datos al editar (por si acaso)
    @Query("SELECT e FROM Emisor e " +
            "LEFT JOIN FETCH e.departamento " +
            "LEFT JOIN FETCH e.distrito " +
            "LEFT JOIN FETCH e.localidad " +
            "LEFT JOIN FETCH e.configuracion " +
            "WHERE e.id = :id")
    Optional<Emisor> findByIdWithDetails(@Param("id") Integer id);
}