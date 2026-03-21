-- Add document_customers table
-- Tabla para información de clientes en documentos

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

-- Agregar campos específicos a la tabla documents para facturas SIFEN
ALTER TABLE documents ADD COLUMN IF NOT EXISTS establecimiento VARCHAR(3);
ALTER TABLE documents ADD COLUMN IF NOT EXISTS punto VARCHAR(3);
ALTER TABLE documents ADD COLUMN IF NOT EXISTS numero VARCHAR(7);
ALTER TABLE documents ADD COLUMN IF NOT EXISTS issue_date TIMESTAMP;
ALTER TABLE documents ADD COLUMN IF NOT EXISTS currency VARCHAR(3);
ALTER TABLE documents ADD COLUMN IF NOT EXISTS total_amount DECIMAL(15,2);
ALTER TABLE documents ADD COLUMN IF NOT EXISTS tipo_documento INTEGER;
ALTER TABLE documents ADD COLUMN IF NOT EXISTS tipo_emision INTEGER;
ALTER TABLE documents ADD COLUMN IF NOT EXISTS tipo_transaccion INTEGER;
ALTER TABLE documents ADD COLUMN IF NOT EXISTS condicion_pago INTEGER;
ALTER TABLE documents ADD COLUMN IF NOT EXISTS cambio DECIMAL(15,8);
ALTER TABLE documents ADD COLUMN IF NOT EXISTS descripcion TEXT;
ALTER TABLE documents ADD COLUMN IF NOT EXISTS codigo_seguridad_aleatorio VARCHAR(9);
ALTER TABLE documents ADD COLUMN IF NOT EXISTS total_redondeo DECIMAL(15,2);

-- Agregar campos específicos a document_items
ALTER TABLE document_items ADD COLUMN IF NOT EXISTS codigo VARCHAR(50);
ALTER TABLE document_items ADD COLUMN IF NOT EXISTS unidad_medida DECIMAL(10,2);
ALTER TABLE document_items ADD COLUMN IF NOT EXISTS iva_tasa INTEGER;
ALTER TABLE document_items ADD COLUMN IF NOT EXISTS iva_afecta INTEGER;
ALTER TABLE document_items ADD COLUMN IF NOT EXISTS base_grav_item DECIMAL(15,8);
ALTER TABLE document_items ADD COLUMN IF NOT EXISTS liq_iva_item DECIMAL(15,8);

-- Agregar campos específicos a document_payments
ALTER TABLE document_payments ADD COLUMN IF NOT EXISTS tipo_pago VARCHAR(10);
ALTER TABLE document_payments ADD COLUMN IF NOT EXISTS monto DECIMAL(15,2);

-- Crear índices adicionales
CREATE INDEX IF NOT EXISTS idx_documents_establecimiento_punto_numero ON documents(establecimiento, punto, numero);
CREATE INDEX IF NOT EXISTS idx_documents_tipo_documento ON documents(tipo_documento);
CREATE INDEX IF NOT EXISTS idx_documents_condicion_pago ON documents(condicion_pago);
CREATE INDEX IF NOT EXISTS idx_document_customers_ruc ON document_customers(ruc);
CREATE INDEX IF NOT EXISTS idx_document_customers_nombre ON document_customers(nombre);

