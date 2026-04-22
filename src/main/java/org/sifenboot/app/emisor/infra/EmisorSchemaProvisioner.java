package org.sifenboot.app.emisor.infra;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
public class EmisorSchemaProvisioner {

    private final JdbcTemplate jdbcTemplate;

    public EmisorSchemaProvisioner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void crearEstructura(String schemaName) {
        // 1. Crear el esquema del emisor
        jdbcTemplate.execute("CREATE SCHEMA IF NOT EXISTS " + schemaName);

        // 2. TABLA MAESTRA: DOCUMENTO
        // Representa el estado actual y los datos base de la factura
        jdbcTemplate.execute(String.format(
                "CREATE TABLE IF NOT EXISTS %s.documento (" +
                        "id BIGSERIAL PRIMARY KEY, " +
                        "cdc VARCHAR(80) UNIQUE, " +
                        "tipo_documento INTEGER, " +      // Ej: 1 (Factura), 2 (Nota de Crédito)
                        "establecimiento VARCHAR(3), " +  // Ej: '001'
                        "punto_expedicion VARCHAR(3), " + // Ej: '001'
                        "numero_documento VARCHAR(15), " +
                        "estado VARCHAR(25) DEFAULT 'RECIBIDO', " + // RECIBIDO, PROCESANDO, APROBADO, RECHAZADO
                        "ultimo_lote VARCHAR(20), " +
                        "xml_firmado TEXT, " +
                        "json_data JSONB, " +
                        "fecha_emision TIMESTAMP, " +
                        "fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                        "fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ")", schemaName));

        // 3. TABLA DE TRAZABILIDAD: DOCUMENTO_EVENTO
        // Bitácora interna de intentos, lotes y respuestas de la SET
        jdbcTemplate.execute(String.format(
                "CREATE TABLE IF NOT EXISTS %s.documento_evento (" +
                        "id BIGSERIAL PRIMARY KEY, " +
                        "documento_id BIGINT REFERENCES %s.documento(id) ON DELETE CASCADE, " +
                        "tipo_evento VARCHAR(50), " +  // Ej: 'ENVIO_LOTE', 'CONSULTA_RESULTADO', 'ERROR_CONEXION'
                        "numero_lote VARCHAR(20), " +
                        "codigo_status VARCHAR(10), " + // El código de respuesta de Sifen (ej: 0300)
                        "mensaje_resultado TEXT, " +
                        "fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ")", schemaName, schemaName));

        // 4. ÍNDICE DE ESTADO (Crucial para el rendimiento del Worker)
        jdbcTemplate.execute(String.format(
                "CREATE INDEX IF NOT EXISTS idx_doc_estado_%s ON %s.documento (estado)",
                schemaName, schemaName));
    }



}