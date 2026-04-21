package org.sifenboot.shell.service;

import org.sifenboot.identity.domain.repository.Certificado;
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
        System.out.println(">>> Buscando certificado para emisor: " + emisorId);
        return certificadoRepository.findByEmisorId(emisorId);
    }


    @Transactional
    public Certificado guardar(Certificado certificado, MultipartFile archivoP12) throws IOException {
        Long emisorId = certificado.getEmisor().getId();
        System.out.println(">>> Iniciando guardado de certificado para Emisor ID: " + emisorId);

        // 1. Buscamos si ya existe para reusar el ID (evita el borrado manual)
        certificadoRepository.findByEmisorId(emisorId).ifPresent(existente -> {
            System.out.println(">>> Certificado previo encontrado (ID: " + existente.getId() + "). Actualizando...");
            certificado.setId(existente.getId());
        });

        // 2. Procesar binario
        if (archivoP12 != null && !archivoP12.isEmpty()) {
            System.out.println(">>> Archivo recibido: " + archivoP12.getOriginalFilename() + " (" + archivoP12.getSize() + " bytes)");
            certificado.setP12Contenido(archivoP12.getBytes());
        } else {
            System.out.println(">>> Advertencia: No se recibió archivo binario .p12");
        }

        certificado.setActivo(true);
        Certificado guardado = certificadoRepository.save(certificado);

        System.out.println(">>> Certificado guardado exitosamente con ID: " + guardado.getId());
        return guardado;
    }




    @Transactional
    public void eliminarPorEmisor(Long emisorId) {
        certificadoRepository.deleteByEmisorId(emisorId);
    }
}