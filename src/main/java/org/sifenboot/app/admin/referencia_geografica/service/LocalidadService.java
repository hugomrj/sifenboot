package org.sifenboot.app.admin.referencia_geografica.service;

import org.sifenboot.app.admin.referencia_geografica.repository.LocalidadRepository;

import org.sifenboot.app.admin.referencia_geografica.model.Localidad;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LocalidadService {

    private final LocalidadRepository localidadRepository;

    public LocalidadService(LocalidadRepository localidadRepository) {
        this.localidadRepository = localidadRepository;
    }

    public List<Localidad> findByDistritoId(Integer distritoId) {
        if (distritoId == null) {
            return List.of();
        }
        return localidadRepository.findByDistritoId(distritoId);
    }
}