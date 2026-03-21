-- SIFEN Database Schema
-- Tablas principales para el sistema SIFEN

-- Tabla de documentos
CREATE TABLE documents (
    id VARCHAR(36) PRIMARY KEY,
    company_id VARCHAR(36) NOT NULL,
    doc_type VARCHAR(10) NOT NULL,
    series VARCHAR(10) NOT NULL,
    number INTEGER NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'RECEIVED',
    idempotency_key VARCHAR(255) NOT NULL,
    track_id VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_state_change TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_event VARCHAR(50),
    cdc VARCHAR(255),
    qr_link TEXT,
    xml_link TEXT,
    last_error TEXT,
    UNIQUE(company_id, idempotency_key),
    UNIQUE(company_id, series, number)
);

-- Tabla de items de documentos
CREATE TABLE document_items (
    id VARCHAR(36) PRIMARY KEY,
    document_id VARCHAR(36) NOT NULL,
    item_code VARCHAR(100),
    description TEXT NOT NULL,
    quantity DECIMAL(10,2) NOT NULL,
    unit_price DECIMAL(15,2) NOT NULL,
    line_total DECIMAL(15,2) NOT NULL,
    iva_rate DECIMAL(5,2),
    iva_amount DECIMAL(15,2),
    unit_measure_code VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE
);

-- Tabla de pagos de documentos
CREATE TABLE document_payments (
    id VARCHAR(36) PRIMARY KEY,
    document_id VARCHAR(36) NOT NULL,
    payment_method_code VARCHAR(10) NOT NULL,
    payment_method_name VARCHAR(100) NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE
);

-- Tabla de eventos de documentos
CREATE TABLE document_events (
    id VARCHAR(36) PRIMARY KEY,
    document_id VARCHAR(36) NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    event_data TEXT,
    metadata JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id VARCHAR(36),
    reason TEXT,
    FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE
);

-- Tabla de outbox para eventos
CREATE TABLE outbox_events (
    id VARCHAR(36) PRIMARY KEY,
    aggregate_id VARCHAR(36) NOT NULL,
    aggregate_type VARCHAR(50) NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    event_data TEXT,
    metadata JSONB,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    processed_at TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    retry_count INTEGER DEFAULT 0,
    last_error TEXT,
    idempotency_key VARCHAR(255) NOT NULL,
    company_id VARCHAR(36) NOT NULL,
    track_id VARCHAR(255) NOT NULL,
    UNIQUE(company_id, idempotency_key)
);

-- Tabla de firmas de documentos
CREATE TABLE document_signatures (
    id VARCHAR(36) PRIMARY KEY,
    company_id VARCHAR(36) NOT NULL,
    document_id VARCHAR(36),
    xml_hash VARCHAR(255) NOT NULL,
    certificate_alias VARCHAR(100) NOT NULL,
    signature_algorithm VARCHAR(50) NOT NULL,
    signature_value TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE SET NULL
);

-- Tabla de certificados de empresas
CREATE TABLE company_certificates (
    id VARCHAR(36) PRIMARY KEY,
    company_id VARCHAR(36) NOT NULL,
    certificate_alias VARCHAR(100) NOT NULL,
    encrypted_certificate TEXT NOT NULL,
    certificate_password TEXT NOT NULL,
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    UNIQUE(company_id, certificate_alias)
);

-- Tabla de timbrados de empresas
CREATE TABLE company_timbrados (
    id VARCHAR(36) PRIMARY KEY,
    company_id VARCHAR(36) NOT NULL,
    timbrado_number VARCHAR(20) NOT NULL,
    valid_from TIMESTAMP NOT NULL,
    valid_to TIMESTAMP NOT NULL,
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(company_id, timbrado_number)
);

-- Índices para optimización
CREATE INDEX idx_documents_company_status ON documents(company_id, status);
CREATE INDEX idx_documents_idempotency ON documents(company_id, idempotency_key);
CREATE INDEX idx_outbox_events_status ON outbox_events(status, created_at);
CREATE INDEX idx_document_events_document ON document_events(document_id, created_at);
CREATE INDEX idx_document_signatures_company ON document_signatures(company_id, created_at);

