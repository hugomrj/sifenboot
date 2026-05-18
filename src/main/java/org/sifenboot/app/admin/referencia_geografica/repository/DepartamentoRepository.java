package org.sifenboot.app.admin.referencia_geografica.repository;

import org.sifenboot.app.admin.referencia_geografica.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Integer> {
}