package org.sifenboot.setup.db;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Component;

@Component
public class DbUtils {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Cambia el esquema de la base de datos para la conexión actual.
     * @param codEmisor Nombre del esquema (se limpia de caracteres extraños).
     */
    public void setSchema(String codEmisor) {
        if (codEmisor == null || codEmisor.isBlank()) {
            return;
        }

        // Limpieza básica para evitar inyección de SQL
        String schema = codEmisor.replaceAll("[^a-zA-Z0-9_]", "");

        // Ejecución del comando nativo de PostgreSQL
        entityManager.createNativeQuery("SET search_path TO " + schema).executeUpdate();
    }
}