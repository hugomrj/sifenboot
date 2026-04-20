package org.sifenboot.shell.service;

import org.sifenboot.shell.model.Departamento;
import org.sifenboot.shell.repository.DepartamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    public DepartamentoService(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    @Transactional(readOnly = true)
    public List<Departamento> findAll() {
        // Retornamos la lista de departamentos para el select del form
        return departamentoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Departamento findById(Integer id) {
        return departamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Departamento no encontrado con ID: " + id));
    }
}