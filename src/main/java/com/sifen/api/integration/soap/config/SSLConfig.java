package com.sifen.api.integration.soap.config;

import com.sifen.api.integration.sifen.config.SifenProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

@Service
public class SSLConfig {

    private final SifenProperties sifenProperties;

    public SSLConfig(SifenProperties sifenProperties) {
        this.sifenProperties = sifenProperties;
    }

    public SSLContext createSSLContext() {
        try {
            String keystorePath = sifenProperties.getCertPath();
            String keystorePassword = sifenProperties.getCertPass();

            if (keystorePath == null || keystorePath.isBlank()) {
                throw new RuntimeException("cert-path no está configurado");
            }
            if (keystorePassword == null) {
                throw new RuntimeException("cert-pass no está configurado");
            }

            Resource resource = resolveKeystoreResource(keystorePath);

            // 💡 No dependas de resource.exists(): abrí el stream y si falla, ahí sabés la verdad.
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (InputStream is = resource.getInputStream()) {
                keyStore.load(is, keystorePassword.toCharArray());
            }

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, keystorePassword.toCharArray());

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());
            return sslContext;

        } catch (Exception e) {
            throw new RuntimeException("Failed to create SSL context: " + e.getMessage(), e);
        }
    }

    private Resource resolveKeystoreResource(String keystorePath) {
        // classpath:
        if (keystorePath.startsWith("classpath:")) {
            String cp = keystorePath.substring("classpath:".length());
            if (cp.startsWith("/")) cp = cp.substring(1);

            ClassPathResource r = new ClassPathResource(cp);
            // acá podés ver exactamente qué está buscando
            if (!r.exists()) {
                throw new RuntimeException(
                        "Archivo PKCS12 no encontrado en classpath. Buscado como: " + r.getDescription()
                                + " | path original: " + keystorePath
                );
            }
            return r;
        }

        // file: explícito
        if (keystorePath.startsWith("file:")) {
            return new FileSystemResource(keystorePath.substring("file:".length()));
        }


        // Windows (C:\ o C:/)
        if (keystorePath.matches("^[A-Za-z]:[\\\\/].*")) {
            return new FileSystemResource(keystorePath);
        }

        // Linux / ruta relativa
        return new FileSystemResource(keystorePath);
    }
}
