package org.sifenboot.shell.service;

import org.sifenboot.shell.model.Emisor;
import org.sifenboot.shell.repository.EmisorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class EmisorService {

    private final EmisorRepository emisorRepository;

    public EmisorService(EmisorRepository emisorRepository) {
        this.emisorRepository = emisorRepository;
    }

    // Estándar: findAll (para colecciones)
    public List<Emisor> findAll() {
        return emisorRepository.findAll();
    }

    // Estándar: findById (para un registro único)
    public Emisor findById(Long id) {
        return emisorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emisor not found with ID: " + id));
    }

    // Estándar: save (sirve para create y update)
    @Transactional
    public Emisor save(Emisor emisor) {
        // Normalización de datos
        if (emisor.getCodEmisor() != null) {
            emisor.setCodEmisor(emisor.getCodEmisor().toLowerCase().trim().replaceAll("\\s+", "-"));
        }

        if (emisor.getRuc() != null) {
            emisor.setRuc(emisor.getRuc().trim());
        }

        if (emisor.getRazonSocial() != null) {
            emisor.setRazonSocial(emisor.getRazonSocial().toUpperCase().trim());
        }

        return emisorRepository.save(emisor);
    }

    // Estándar: deleteById
    @Transactional
    public void deleteById(Long id) {
        if (!emisorRepository.existsById(id)) {
            throw new RuntimeException("Cannot delete: Emisor not found with ID: " + id);
        }
        emisorRepository.deleteById(id);
    }
}