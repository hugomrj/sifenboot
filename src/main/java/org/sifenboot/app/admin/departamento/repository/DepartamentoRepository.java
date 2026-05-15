package org.sifenboot.app.admin.departamento.repository;

import org.sifenboot.app.admin.departamento.model.Departamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartamentoRepository extends JpaRepository<Departamento, Integer> {
}