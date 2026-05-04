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
        String sql = String.format("CREATE SCHEMA IF NOT EXISTS \"%s\"", schemaName);
        jdbcTemplate.execute(sql);

        // 2. TABLA MAESTRA: DOCUMENTO
        jdbcTemplate.execute(String.format(
                "CREATE TABLE IF NOT EXISTS %s.documentos (" +
                        "id BIGSERIAL PRIMARY KEY, " +
                        "cdc VARCHAR(80) UNIQUE, " +
                        "tipo_documento INTEGER NOT NULL, " +
                        "establecimiento VARCHAR(3) NOT NULL, " +
                        "punto_expedicion VARCHAR(3) NOT NULL, " +
                        "numero_documento VARCHAR(15) NOT NULL, " +
                        "nombre_receptor VARCHAR(255), " +
                        "ruc_receptor VARCHAR(20), " +
                        "estado_id SMALLINT NOT NULL DEFAULT 1, " +
                        "numero_lote VARCHAR(20), " +
                        "monto_total NUMERIC(15,2), " +
                        "xml_enviado TEXT, " +
                        "xml_respuesta TEXT, " +
                        "json_data JSONB, " +
                        "fecha_emision TIMESTAMP NOT NULL, " +
                        "fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                        "fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                        "CONSTRAINT fk_doc_estado FOREIGN KEY (estado_id) REFERENCES public.estados_documento(id)" +
                        ")", schemaName));

        // 3. TABLA DE RESPUESTAS (Auditoría de comunicación con la SET)
        jdbcTemplate.execute(String.format(
                "CREATE TABLE IF NOT EXISTS %s.documento_respuestas (" +
                        "id BIGSERIAL PRIMARY KEY, " +
                        "documento_id BIGINT NOT NULL REFERENCES %s.documentos(id) ON DELETE CASCADE, " +
                        "numero_lote VARCHAR(20), " +
                        "tipo_operacion VARCHAR(30) NOT NULL, " + // 'ENVIO_LOTE', 'CONSULTA_LOTE'
                        "codigo_respuesta VARCHAR(10), " +       // '0300', '0302'
                        "mensaje_respuesta TEXT, " +
                        "xml_respuesta_cruda TEXT, " +
                        "fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                        ")", schemaName, schemaName));

        // 4. Índices para el rendimiento del Worker (Polling)
        jdbcTemplate.execute(String.format(
                "CREATE INDEX IF NOT EXISTS idx_doc_estado_%s ON %s.documentos (estado_id)",
                schemaName, schemaName));

        // Índice para agrupar por lotes al consultar
        jdbcTemplate.execute(String.format(
                "CREATE INDEX IF NOT EXISTS idx_doc_lote_%s ON %s.documentos (numero_lote) WHERE numero_lote IS NOT NULL",
                schemaName, schemaName));

        jdbcTemplate.execute(String.format(
                "CREATE INDEX IF NOT EXISTS idx_documentos_nombre_receptor ON %s.documentos (nombre_receptor)",
                schemaName));

    }
}