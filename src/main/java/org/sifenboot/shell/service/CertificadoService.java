package org.sifenboot.shell.service;

import org.sifenboot.shell.model.Certificado;
import org.sifenboot.shell.model.Emisor; // Importante añadir el import
import org.sifenboot.shell.repository.CertificadoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class CertificadoService {

    private final CertificadoRepository certificadoRepository;

    public CertificadoService(CertificadoRepository certificadoRepository) {
        this.certificadoRepository = certificadoRepository;
    }

    public Optional<Certificado> getByEmisor(Long emisorId) {
        return certificadoRepository.findByEmisorId(emisorId);
    }

    @Transactional
    public Certificado guardar(Certificado certificado, MultipartFile archivoP12) throws IOException {
        // 1. Si ya existe un certificado para este emisor, lo eliminamos (Reemplazo Total)
        if (certificado.getEmisor() != null && certificadoRepository.existsByEmisorId(certificado.getEmisor().getId())) {
            certificadoRepository.deleteByEmisorId(certificado.getEmisor().getId());
            certificadoRepository.flush(); // Aseguramos que se borre antes de insertar
        }

        // 2. Procesar el archivo binario
        if (archivoP12 != null && !archivoP12.isEmpty()) {
            certificado.setP12Contenido(archivoP12.getBytes());
        }

        // 3. Normalización de ambiente (Ahora en el Emisor)
        // Accedemos a través de la relación para evitar el error de compilación
        if (certificado.getEmisor() != null) {
            Emisor emisor = certificado.getEmisor();
            if (emisor.getAmbiente() == null || emisor.getAmbiente().isEmpty()) {
                emisor.setAmbiente("test");
            }
        }

        certificado.setActivo(true);
        return certificadoRepository.save(certificado);
    }

    @Transactional
    public void eliminarPorEmisor(Long emisorId) {
        certificadoRepository.deleteByEmisorId(emisorId);
    }
}