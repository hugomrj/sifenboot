-- SIFEN Results and Events Tables
-- Tablas para persistir resultados y eventos de SIFEN

-- Tabla de resultados SIFEN
CREATE TABLE IF NOT EXISTS sifen_results (
    id VARCHAR(36) PRIMARY KEY,
    document_id VARCHAR(36) NOT NULL,
    company_id VARCHAR(36) NOT NULL,
    status VARCHAR(20) NOT NULL,
    cdc VARCHAR(255),
    qr_link TEXT,
    xml_link TEXT,
    xml_signed TEXT,
    rejection_reason TEXT,
    sifen_response TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE
);

-- Tabla de eventos SIFEN
CREATE TABLE IF NOT EXISTS sifen_events (
    id VARCHAR(36) PRIMARY KEY,
    document_id VARCHAR(36) NOT NULL,
    company_id VARCHAR(36) NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    event_data TEXT,
    sifen_response TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE
);

-- Tabla de trazabilidad SIFEN
CREATE TABLE IF NOT EXISTS sifen_traceability (
    id VARCHAR(36) PRIMARY KEY,
    document_id VARCHAR(36) NOT NULL,
    company_id VARCHAR(36) NOT NULL,
    operation_type VARCHAR(50) NOT NULL,
    operation_status VARCHAR(20) NOT NULL,
    sifen_reference VARCHAR(255),
    request_data TEXT,
    response_data TEXT,
    processing_time_ms INTEGER,
    error_details TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE
);

-- Tabla de métricas SIFEN
CREATE TABLE IF NOT EXISTS sifen_metrics (
    id VARCHAR(36) PRIMARY KEY,
    company_id VARCHAR(36) NOT NULL,
    metric_date DATE NOT NULL,
    total_documents INTEGER DEFAULT 0,
    accepted_documents INTEGER DEFAULT 0,
    rejected_documents INTEGER DEFAULT 0,
    failed_documents INTEGER DEFAULT 0,
    average_processing_time_ms INTEGER DEFAULT 0,
    total_processing_time_ms BIGINT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(company_id, metric_date)
);

-- Tabla de configuración SIFEN por empresa
CREATE TABLE IF NOT EXISTS sifen_company_config (
    id VARCHAR(36) PRIMARY KEY,
    company_id VARCHAR(36) NOT NULL UNIQUE,
    sifen_environment VARCHAR(20) NOT NULL DEFAULT 'TEST',
    auto_retry BOOLEAN DEFAULT true,
    max_retries INTEGER DEFAULT 3,
    retry_delay_ms INTEGER DEFAULT 5000,
    timeout_ms INTEGER DEFAULT 30000,
    notification_email VARCHAR(255),
    webhook_url TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (company_id) REFERENCES company(id) ON DELETE CASCADE
);

-- Crear índices para optimizar consultas
CREATE INDEX IF NOT EXISTS idx_sifen_results_document_id ON sifen_results(document_id);
CREATE INDEX IF NOT EXISTS idx_sifen_results_company_id ON sifen_results(company_id);
CREATE INDEX IF NOT EXISTS idx_sifen_results_status ON sifen_results(status);
CREATE INDEX IF NOT EXISTS idx_sifen_results_created_at ON sifen_results(created_at);

CREATE INDEX IF NOT EXISTS idx_sifen_events_document_id ON sifen_events(document_id);
CREATE INDEX IF NOT EXISTS idx_sifen_events_company_id ON sifen_events(company_id);
CREATE INDEX IF NOT EXISTS idx_sifen_events_event_type ON sifen_events(event_type);
CREATE INDEX IF NOT EXISTS idx_sifen_events_created_at ON sifen_events(created_at);

CREATE INDEX IF NOT EXISTS idx_sifen_traceability_document_id ON sifen_traceability(document_id);
CREATE INDEX IF NOT EXISTS idx_sifen_traceability_company_id ON sifen_traceability(company_id);
CREATE INDEX IF NOT EXISTS idx_sifen_traceability_operation_type ON sifen_traceability(operation_type);
CREATE INDEX IF NOT EXISTS idx_sifen_traceability_created_at ON sifen_traceability(created_at);

CREATE INDEX IF NOT EXISTS idx_sifen_metrics_company_id ON sifen_metrics(company_id);
CREATE INDEX IF NOT EXISTS idx_sifen_metrics_metric_date ON sifen_metrics(metric_date);

-- Insertar configuración por defecto para empresas existentes
INSERT INTO sifen_company_config (id, company_id, sifen_environment, auto_retry, max_retries, retry_delay_ms, timeout_ms)
SELECT 
    gen_random_uuid()::text,
    id,
    'TEST',
    true,
    3,
    5000,
    30000
FROM company
WHERE id NOT IN (SELECT company_id FROM sifen_company_config);

-- Crear función para actualizar métricas automáticamente
CREATE OR REPLACE FUNCTION update_sifen_metrics()
RETURNS TRIGGER AS $$
BEGIN
    -- Actualizar métricas del día
    INSERT INTO sifen_metrics (
        id, company_id, metric_date, total_documents, accepted_documents, 
        rejected_documents, failed_documents, created_at, updated_at
    )
    SELECT 
        gen_random_uuid()::text,
        NEW.company_id,
        CURRENT_DATE,
        1,
        CASE WHEN NEW.status = 'ACCEPTED' THEN 1 ELSE 0 END,
        CASE WHEN NEW.status = 'REJECTED' THEN 1 ELSE 0 END,
        CASE WHEN NEW.status = 'FAILED' THEN 1 ELSE 0 END,
        CURRENT_TIMESTAMP,
        CURRENT_TIMESTAMP
    ON CONFLICT (company_id, metric_date) 
    DO UPDATE SET
        total_documents = sifen_metrics.total_documents + 1,
        accepted_documents = sifen_metrics.accepted_documents + 
            CASE WHEN NEW.status = 'ACCEPTED' THEN 1 ELSE 0 END,
        rejected_documents = sifen_metrics.rejected_documents + 
            CASE WHEN NEW.status = 'REJECTED' THEN 1 ELSE 0 END,
        failed_documents = sifen_metrics.failed_documents + 
            CASE WHEN NEW.status = 'FAILED' THEN 1 ELSE 0 END,
        updated_at = CURRENT_TIMESTAMP;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Crear trigger para actualizar métricas automáticamente
CREATE TRIGGER trigger_update_sifen_metrics
    AFTER INSERT ON sifen_results
    FOR EACH ROW
    EXECUTE FUNCTION update_sifen_metrics();

-- Crear vista para estadísticas de SIFEN
CREATE OR REPLACE VIEW sifen_statistics AS
SELECT 
    sr.company_id,
    DATE(sr.created_at) as date,
    COUNT(*) as total_documents,
    COUNT(CASE WHEN sr.status = 'ACCEPTED' THEN 1 END) as accepted_documents,
    COUNT(CASE WHEN sr.status = 'REJECTED' THEN 1 END) as rejected_documents,
    COUNT(CASE WHEN sr.status = 'FAILED' THEN 1 END) as failed_documents,
    ROUND(
        COUNT(CASE WHEN sr.status = 'ACCEPTED' THEN 1 END) * 100.0 / COUNT(*), 2
    ) as success_rate
FROM sifen_results sr
GROUP BY sr.company_id, DATE(sr.created_at)
ORDER BY sr.company_id, date DESC;

-- Crear vista para eventos recientes
CREATE OR REPLACE VIEW sifen_recent_events AS
SELECT 
    se.id,
    se.document_id,
    se.company_id,
    se.event_type,
    se.event_data,
    se.created_at,
    d.series,
    d.number,
    d.doc_type
FROM sifen_events se
JOIN documents d ON se.document_id = d.id
ORDER BY se.created_at DESC
LIMIT 1000;
