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

    public List<Emisor> findAll() {
        return emisorRepository.findAll();
    }

    public Emisor findById(Long id) {
        return emisorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emisor no encontrado con ID: " + id));
    }

    @Transactional
    public Emisor save(Emisor emisor) {
        // 1. Normalización de Identificadores (CodEmisor y RUC)
        if (emisor.getCodEmisor() != null && !emisor.getCodEmisor().isBlank()) {
            emisor.setCodEmisor(emisor.getCodEmisor().toLowerCase().trim().replaceAll("\\s+", "-"));
        } else if (emisor.getRuc() != null) {
            // Si no hay código, usamos el RUC limpio como slug
            emisor.setCodEmisor(emisor.getRuc().replaceAll("[^0-9]", ""));
        }

        if (emisor.getRuc() != null) {
            emisor.setRuc(emisor.getRuc().trim().toUpperCase());
        }

        // 2. Validación de Campos Obligatorios Fiscales
        if (emisor.getFechaInicioTimbrado() == null) {
            throw new IllegalArgumentException("La fecha de inicio de timbrado es obligatoria.");
        }

        // 3. NUEVO: Normalización de Credenciales SSET (Ambiente, CSC, ID CSC)
        if (emisor.getAmbiente() == null || emisor.getAmbiente().isBlank()) {
            emisor.setAmbiente("test");
        } else {
            emisor.setAmbiente(emisor.getAmbiente().toLowerCase().trim());
        }

        if (emisor.getIdCsc() != null) {
            emisor.setIdCsc(emisor.getIdCsc().trim());
        } else {
            throw new IllegalArgumentException("El ID CSC es obligatorio para la integración con SIFEN.");
        }

        if (emisor.getCsc() != null) {
            emisor.setCsc(emisor.getCsc().trim());
        } else {
            throw new IllegalArgumentException("El código CSC es obligatorio para generar el QR y CDC.");
        }

        // 4. Normalización de Textos de Negocio
        if (emisor.getRazonSocial() != null) {
            emisor.setRazonSocial(emisor.getRazonSocial().toUpperCase().trim());
        }

        if (emisor.getNombreFantasia() != null) {
            emisor.setNombreFantasia(emisor.getNombreFantasia().trim());
        }

        // 5. Limpieza de Contacto y Otros
        if (emisor.getEmail() != null) {
            emisor.setEmail(emisor.getEmail().toLowerCase().trim());
        }

        if (emisor.getTipoContribuyente() == null) {
            emisor.setTipoContribuyente(1); // Default: Persona Física o General
        }

        return emisorRepository.save(emisor);
    }

    @Transactional
    public void deleteById(Long id) {
        if (!emisorRepository.existsById(id)) {
            throw new RuntimeException("Error: El emisor con ID " + id + " no existe.");
        }
        emisorRepository.deleteById(id);
    }


}