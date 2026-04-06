package org.sifenboot.core.integration.soap.config;

import org.springframework.stereotype.Service;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

@Service
public class SSLConfig {

    /**
     * Crea un SSLContext dinámico utilizando los datos del certificado almacenados en la DB.
     *
     * @param p12Contenido El byte[] (blob) del archivo .p12
     * @param p12Password  La contraseña del certificado
     * @return SSLContext configurado para Sifen
     */
    public SSLContext createSSLContext(byte[] p12Contenido, String p12Password) {
        if (p12Contenido == null || p12Contenido.length == 0) {
            throw new IllegalArgumentException("El contenido del certificado (.p12) es nulo o está vacío.");
        }
        if (p12Password == null) {
            throw new IllegalArgumentException("La contraseña del certificado es requerida.");
        }

        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            try (InputStream is = new ByteArrayInputStream(p12Contenido)) {
                keyStore.load(is, p12Password.toCharArray());
            }

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, p12Password.toCharArray());

            SSLContext sslContext = SSLContext.getInstance("TLSv1.2"); // Recomendado especificar versión para Sifen
            sslContext.init(kmf.getKeyManagers(), null, new SecureRandom());

            return sslContext;

        } catch (Exception e) {
            throw new RuntimeException("Error al crear SSLContext desde la base de datos: " + e.getMessage(), e);
        }
    }

    /**
     * Método legacy neutralizado.
     * Evita que la aplicación falle al arrancar si algún componente todavía lo invoca en un @PostConstruct.
     */
    public SSLContext createSSLContext() {
        // Retornamos null en lugar de lanzar excepción para permitir que el contexto de Spring suba.
        return null;
    }
}