-- SIFEN Database Schema
-- Script completo para crear todas las tablas necesarias

-- Tabla de documentos
CREATE TABLE IF NOT EXISTS documents (
    id VARCHAR(36) PRIMARY KEY,
    company_id VARCHAR(36) NOT NULL,
    doc_type VARCHAR(10) NOT NULL,
    series VARCHAR(10),
    number INTEGER,
    establecimiento VARCHAR(3),
    punto VARCHAR(3),
    numero VARCHAR(7),
    issue_date TIMESTAMP,
    currency VARCHAR(3),
    total_amount DECIMAL(15,2),
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
    tipo_documento INTEGER,
    tipo_emision INTEGER,
    tipo_transaccion INTEGER,
    condicion_pago INTEGER,
    cambio DECIMAL(15,8),
    descripcion TEXT,
    codigo_seguridad_aleatorio VARCHAR(9),
    total_redondeo DECIMAL(15,2),
    UNIQUE(company_id, idempotency_key),
    UNIQUE(company_id, series, number)
);

-- Tabla de items de documentos
CREATE TABLE IF NOT EXISTS document_items (
    id VARCHAR(36) PRIMARY KEY,
    document_id VARCHAR(36) NOT NULL,
    item_code VARCHAR(100),
    codigo VARCHAR(50),
    description TEXT NOT NULL,
    descripcion TEXT,
    quantity DECIMAL(10,2) NOT NULL,
    unit_price DECIMAL(15,2) NOT NULL,
    line_total DECIMAL(15,2) NOT NULL,
    iva_rate DECIMAL(5,2),
    iva_amount DECIMAL(15,2),
    unit_measure_code VARCHAR(10),
    unidad_medida DECIMAL(10,2),
    iva_tasa INTEGER,
    iva_afecta INTEGER,
    base_grav_item DECIMAL(15,8),
    liq_iva_item DECIMAL(15,8),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE
);

-- Tabla de pagos de documentos
CREATE TABLE IF NOT EXISTS document_payments (
    id VARCHAR(36) PRIMARY KEY,
    document_id VARCHAR(36) NOT NULL,
    payment_method_code VARCHAR(10) NOT NULL,
    payment_method_name VARCHAR(100) NOT NULL,
    amount DECIMAL(15,2) NOT NULL,
    tipo_pago VARCHAR(10),
    monto DECIMAL(15,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE
);

-- Tabla de clientes de documentos
CREATE TABLE IF NOT EXISTS document_customers (
    id VARCHAR(36) PRIMARY KEY,
    document_id VARCHAR(36) NOT NULL,
    ruc VARCHAR(20),
    nombre VARCHAR(200),
    direccion VARCHAR(200),
    cpais VARCHAR(3),
    correo VARCHAR(100),
    num_casa INTEGER,
    diplomatico BOOLEAN,
    dncp INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (document_id) REFERENCES documents(id) ON DELETE CASCADE
);

-- Tabla de eventos de documentos
CREATE TABLE IF NOT EXISTS document_events (
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
CREATE TABLE IF NOT EXISTS outbox_events (
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
CREATE TABLE IF NOT EXISTS document_signatures (
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
CREATE TABLE IF NOT EXISTS company_certificates (
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
CREATE TABLE IF NOT EXISTS company_timbrados (
    id VARCHAR(36) PRIMARY KEY,
    company_id VARCHAR(36) NOT NULL,
    timbrado_number VARCHAR(20) NOT NULL,
    valid_from TIMESTAMP NOT NULL,
    valid_to TIMESTAMP NOT NULL,
    active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(company_id, timbrado_number)
);

-- Tablas de lookup
CREATE TABLE IF NOT EXISTS metodo_pago (
    codigo VARCHAR(10) PRIMARY KEY,
    metodo VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS unidad_medida (
    codigo VARCHAR(10) PRIMARY KEY,
    unidad VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS tipo_transaccion (
    codigo VARCHAR(10) PRIMARY KEY,
    descripcion VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS moneda (
    codigoISO VARCHAR(3) PRIMARY KEY,
    moneda VARCHAR(100) NOT NULL,
    simbolo VARCHAR(10) NOT NULL
);

-- Insertar datos de lookup
INSERT INTO metodo_pago (codigo, metodo) VALUES
('1', 'Efectivo'),
('2', 'Cheque'),
('3', 'Tarjeta de crédito'),
('4', 'Tarjeta de débito'),
('5', 'Transferencia'),
('6', 'Giro'),
('7', 'Billetera electrónica'),
('8', 'Tarjeta empresarial'),
('9', 'Vale'),
('10', 'Retención'),
('11', 'Pago por anticipo'),
('12', 'Valor fiscal'),
('13', 'Valor comercial'),
('14', 'Compensación'),
('15', 'Permuta'),
('16', 'Pago bancario'),
('17', 'Pago Móvil'),
('18', 'Donación'),
('19', 'Promoción'),
('20', 'Consumo Interno'),
('21', 'Pago Electrónico'),
('99', 'Otro')
ON CONFLICT (codigo) DO NOTHING;

INSERT INTO unidad_medida (codigo, unidad) VALUES
('77', 'Unidad (UNI)'),
('83', 'Kilogramos (kg)'),
('87', 'Metros (m)'),
('2366', 'Costo por Mil (CPM)'),
('2329', 'Unidad Internacional (UI)'),
('110', 'Metros cúbicos (M3)'),
('86', 'Gramos (g)'),
('89', 'Litros (LT)'),
('90', 'Miligramos (MG)'),
('91', 'Centímetros (CM)'),
('92', 'Centímetros cuadrados (CM2)'),
('93', 'Centímetros cúbicos (CM3)'),
('94', 'Pulgadas (PUL)'),
('96', 'Milímetros cuadrados (MM2)'),
('79', 'Kilogramos s/ metro cuadrado (kg/m²)'),
('97', 'Año (AA)'),
('98', 'Mes (ME)'),
('99', 'Tonelada (TN)'),
('100', 'Hora (Hs)'),
('101', 'Minuto (Mi)'),
('104', 'Determinación (DET)'),
('103', 'Yardas (Ya)'),
('625', 'Kilómetros (Km)'),
('660', 'Metro lineal (ml)')
ON CONFLICT (codigo) DO NOTHING;

INSERT INTO tipo_transaccion (codigo, descripcion) VALUES
('1', 'Venta de mercadería'),
('2', 'Prestación de servicios'),
('3', 'Mixto (Venta de mercadería y servicios)'),
('4', 'Venta de activo fijo'),
('5', 'Venta de divisas'),
('6', 'Compra de divisas'),
('7', 'Promoción o entrega de muestras'),
('8', 'Donación'),
('9', 'Anticipo'),
('10', 'Compra de productos'),
('11', 'Compra de servicios'),
('12', 'Venta de crédito fiscal'),
('13', 'Muestras médicas (Art. 3 RG 24/2014)')
ON CONFLICT (codigo) DO NOTHING;

INSERT INTO moneda (codigoISO, moneda, simbolo) VALUES
('USD', 'Dólar estadounidense', '$'),
('EUR', 'Euro', '€'),
('PYG', 'Guaraní paraguayo', '₲'),
('ARS', 'Peso argentino', '$'),
('BRL', 'Real brasileño', 'R$')
ON CONFLICT (codigoISO) DO NOTHING;

-- Crear índices
CREATE INDEX IF NOT EXISTS idx_documents_company_status ON documents(company_id, status);
CREATE INDEX IF NOT EXISTS idx_documents_idempotency ON documents(company_id, idempotency_key);
CREATE INDEX IF NOT EXISTS idx_documents_establecimiento_punto_numero ON documents(establecimiento, punto, numero);
CREATE INDEX IF NOT EXISTS idx_documents_tipo_documento ON documents(tipo_documento);
CREATE INDEX IF NOT EXISTS idx_documents_condicion_pago ON documents(condicion_pago);
CREATE INDEX IF NOT EXISTS idx_outbox_events_status ON outbox_events(status, created_at);
CREATE INDEX IF NOT EXISTS idx_document_events_document ON document_events(document_id, created_at);
CREATE INDEX IF NOT EXISTS idx_document_signatures_company ON document_signatures(company_id, created_at);
CREATE INDEX IF NOT EXISTS idx_document_customers_ruc ON document_customers(ruc);
CREATE INDEX IF NOT EXISTS idx_document_customers_nombre ON document_customers(nombre);

