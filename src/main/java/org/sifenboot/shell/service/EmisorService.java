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

    @Transactional
    public Emisor save(Emisor emisor) {
        // 1. Normalización de Identificadores
        if (emisor.getCodEmisor() != null) {
            // "Casa Central" -> "casa-central"
            emisor.setCodEmisor(emisor.getCodEmisor().toLowerCase().trim().replaceAll("\\s+", "-"));
        } else {
            // Si es nuevo y no tiene código, usamos el RUC sin guiones como fallback
            emisor.setCodEmisor(emisor.getRuc().replaceAll("[^0-9]", ""));
        }

        if (emisor.getRuc() != null) {
            emisor.setRuc(emisor.getRuc().trim().toUpperCase());
        }

        // 2. Normalización de Textos
        if (emisor.getRazonSocial() != null) {
            emisor.setRazonSocial(emisor.getRazonSocial().toUpperCase().trim());
        }

        if (emisor.getNombreFantasia() != null) {
            emisor.setNombreFantasia(emisor.getNombreFantasia().trim());
        }

        // 3. Limpieza de Contacto
        if (emisor.getEmail() != null) {
            emisor.setEmail(emisor.getEmail().toLowerCase().trim());
        }

        // 4. Auditoría simple / Defaults
        if (emisor.getTipoContribuyente() == null) {
            emisor.setTipoContribuyente(1); // Default: Persona Física
        }

        return emisorRepository.save(emisor);
    }

    @Transactional
    public void deleteById(Long id) {
        // Validamos existencia antes de intentar borrar
        if (!emisorRepository.existsById(id)) {
            throw new RuntimeException("Error: El emisor con ID " + id + " no existe.");
        }
        emisorRepository.deleteById(id);
    }

    /**
     * Útil para validar antes de guardar en el Controller
     */
    public boolean existeRuc(String ruc) {
        // Deberías agregar 'existsByRuc' en tu EmisorRepository
        // return emisorRepository.existsByRuc(ruc);
        return false;
    }
}