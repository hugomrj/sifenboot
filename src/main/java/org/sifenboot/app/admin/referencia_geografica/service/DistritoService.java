package org.sifenboot.app.admin.referencia_geografica.service;

import org.sifenboot.app.admin.referencia_geografica.repository.DistritoRepository;
import org.sifenboot.app.admin.referencia_geografica.model.Distrito;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class DistritoService {

    private final DistritoRepository distritoRepository;

    public DistritoService(DistritoRepository distritoRepository) {
        this.distritoRepository = distritoRepository;
    }

    public List<Distrito> findByDepartamentoId(Integer departamentoId) {
        if (departamentoId == null) {
            return List.of();
        }
        return distritoRepository.findByDepartamentoId(departamentoId);
    }
}