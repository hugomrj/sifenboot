-- V2: Optimización de índices de base de datos para SIFEN v150
-- Basado en análisis de consultas frecuentes y patrones de acceso

-- ==============================================
-- ÍNDICES PARA TABLA DOCUMENTS (Consultas más frecuentes)
-- ==============================================

-- Índice compuesto para consultas por empresa y rango de fechas
CREATE INDEX IF NOT EXISTS idx_documents_company_date_range 
ON documents(company_id, issue_date DESC) 
WHERE issue_date IS NOT NULL;

-- Índice para consultas por tipo de documento y empresa
CREATE INDEX IF NOT EXISTS idx_documents_company_doc_type 
ON documents(company_id, doc_type, created_at DESC);

-- Índice para consultas por estado y empresa (más frecuente)
CREATE INDEX IF NOT EXISTS idx_documents_company_status_created 
ON documents(company_id, status, created_at DESC);

-- Índice para consultas por serie y número (búsquedas específicas)
CREATE INDEX IF NOT EXISTS idx_documents_series_number 
ON documents(company_id, series, number) 
WHERE series IS NOT NULL AND number IS NOT NULL;

-- Índice para consultas por CDC (Código de Control)
CREATE INDEX IF NOT EXISTS idx_documents_cdc 
ON documents(cdc) 
WHERE cdc IS NOT NULL;

-- Índice para consultas por track_id (seguimiento)
CREATE INDEX IF NOT EXISTS idx_documents_track_id 
ON documents(track_id) 
WHERE track_id IS NOT NULL;

-- ==============================================
-- ÍNDICES PARA TABLA DOCUMENT_ITEMS
-- ==============================================

-- Índice para consultas por documento (más frecuente)
CREATE INDEX IF NOT EXISTS idx_document_items_document_id 
ON document_items(document_id, created_at);

-- Índice para consultas por código de item
CREATE INDEX IF NOT EXISTS idx_document_items_item_code 
ON document_items(item_code) 
WHERE item_code IS NOT NULL;

-- Índice para consultas por descripción (búsquedas de texto)
CREATE INDEX IF NOT EXISTS idx_document_items_description 
ON document_items USING gin(to_tsvector('spanish', description));

-- ==============================================
-- ÍNDICES PARA TABLA DOCUMENT_CUSTOMERS
-- ==============================================

-- Índice para consultas por RUC (más frecuente)
CREATE INDEX IF NOT EXISTS idx_document_customers_ruc_company 
ON document_customers(ruc, document_id) 
WHERE ruc IS NOT NULL;

-- Índice para consultas por nombre (búsquedas de texto)
CREATE INDEX IF NOT EXISTS idx_document_customers_nombre 
ON document_customers USING gin(to_tsvector('spanish', nombre)) 
WHERE nombre IS NOT NULL;

-- Índice para consultas por documento
CREATE INDEX IF NOT EXISTS idx_document_customers_document_id 
ON document_customers(document_id);

-- ==============================================
-- ÍNDICES PARA TABLA DOCUMENT_PAYMENTS
-- ==============================================

-- Índice para consultas por documento
CREATE INDEX IF NOT EXISTS idx_document_payments_document_id 
ON document_payments(document_id, created_at);

-- Índice para consultas por método de pago
CREATE INDEX IF NOT EXISTS idx_document_payments_method 
ON document_payments(payment_method_code, created_at);

-- ==============================================
-- ÍNDICES PARA TABLA OUTBOX_EVENTS
-- ==============================================

-- Índice para procesamiento de eventos pendientes (más crítico)
CREATE INDEX IF NOT EXISTS idx_outbox_events_pending_processing 
ON outbox_events(status, created_at, retry_count) 
WHERE status = 'PENDING';

-- Índice para consultas por empresa y tipo de evento
CREATE INDEX IF NOT EXISTS idx_outbox_events_company_event_type 
ON outbox_events(company_id, event_type, created_at DESC);

-- Índice para consultas por aggregate_id
CREATE INDEX IF NOT EXISTS idx_outbox_events_aggregate_id 
ON outbox_events(aggregate_id, created_at DESC);

-- Índice para limpieza de eventos procesados
CREATE INDEX IF NOT EXISTS idx_outbox_events_processed_cleanup 
ON outbox_events(processed_at) 
WHERE status = 'PROCESSED' AND processed_at IS NOT NULL;

-- ==============================================
-- ÍNDICES PARA TABLA DOCUMENT_EVENTS
-- ==============================================

-- Índice para consultas por documento y tipo de evento
CREATE INDEX IF NOT EXISTS idx_document_events_document_type 
ON document_events(document_id, event_type, created_at DESC);

-- Índice para consultas por usuario
CREATE INDEX IF NOT EXISTS idx_document_events_user_id 
ON document_events(user_id, created_at DESC) 
WHERE user_id IS NOT NULL;

-- Índice para consultas por rango de fechas
CREATE INDEX IF NOT EXISTS idx_document_events_date_range 
ON document_events(created_at DESC);

-- ==============================================
-- ÍNDICES PARA TABLA DOCUMENT_SIGNATURES
-- ==============================================

-- Índice para consultas por empresa y fecha
CREATE INDEX IF NOT EXISTS idx_document_signatures_company_date 
ON document_signatures(company_id, created_at DESC);

-- Índice para consultas por documento
CREATE INDEX IF NOT EXISTS idx_document_signatures_document_id 
ON document_signatures(document_id) 
WHERE document_id IS NOT NULL;

-- Índice para consultas por alias de certificado
CREATE INDEX IF NOT EXISTS idx_document_signatures_certificate 
ON document_signatures(certificate_alias, created_at DESC);

-- ==============================================
-- ÍNDICES PARA TABLA COMPANY_CERTIFICATES
-- ==============================================

-- Índice para consultas por empresa y estado activo
CREATE INDEX IF NOT EXISTS idx_company_certificates_active 
ON company_certificates(company_id, active, expires_at) 
WHERE active = true;

-- Índice para consultas por alias
CREATE INDEX IF NOT EXISTS idx_company_certificates_alias 
ON company_certificates(company_id, certificate_alias);

-- ==============================================
-- ÍNDICES PARA TABLA COMPANY_TIMBRADOS
-- ==============================================

-- Índice para consultas por empresa y estado activo
CREATE INDEX IF NOT EXISTS idx_company_timbrados_active 
ON company_timbrados(company_id, active, valid_from, valid_to) 
WHERE active = true;

-- Índice para consultas por número de timbrado
CREATE INDEX IF NOT EXISTS idx_company_timbrados_number 
ON company_timbrados(company_id, timbrado_number);

-- ==============================================
-- ÍNDICES PARA TABLAS DE LOOKUP (Optimización de consultas frecuentes)
-- ==============================================

-- Índice para consultas por código en método_pago
CREATE INDEX IF NOT EXISTS idx_metodo_pago_codigo 
ON metodo_pago(codigo);

-- Índice para consultas por código en unidad_medida
CREATE INDEX IF NOT EXISTS idx_unidad_medida_codigo 
ON unidad_medida(codigo);

-- Índice para consultas por código en tipo_transaccion
CREATE INDEX IF NOT EXISTS idx_tipo_transaccion_codigo 
ON tipo_transaccion(codigo);

-- Índice para consultas por código ISO en moneda
CREATE INDEX IF NOT EXISTS idx_moneda_codigo_iso 
ON moneda(codigoISO);

-- ==============================================
-- ÍNDICES PARCIALES PARA OPTIMIZACIÓN ESPECÍFICA
-- ==============================================

-- Índice para documentos activos (no cancelados/eliminados)
CREATE INDEX IF NOT EXISTS idx_documents_active 
ON documents(company_id, created_at DESC) 
WHERE status NOT IN ('CANCELLED', 'DELETED', 'INUTILIZED');

-- Índice para documentos con errores
CREATE INDEX IF NOT EXISTS idx_documents_with_errors 
ON documents(company_id, created_at DESC) 
WHERE last_error IS NOT NULL;

-- Índice para documentos pendientes de procesamiento
CREATE INDEX IF NOT EXISTS idx_documents_pending 
ON documents(company_id, created_at) 
WHERE status IN ('RECEIVED', 'VALIDATING', 'XML_READY', 'SIGNED');

-- ==============================================
-- ESTADÍSTICAS DE BASE DE DATOS
-- ==============================================

-- Actualizar estadísticas para optimizar el planificador de consultas
ANALYZE documents;
ANALYZE document_items;
ANALYZE document_customers;
ANALYZE document_payments;
ANALYZE document_events;
ANALYZE outbox_events;
ANALYZE document_signatures;
ANALYZE company_certificates;
ANALYZE company_timbrados;
ANALYZE metodo_pago;
ANALYZE unidad_medida;
ANALYZE tipo_transaccion;
ANALYZE moneda;

-- ==============================================
-- COMENTARIOS PARA DOCUMENTACIÓN
-- ==============================================

COMMENT ON INDEX idx_documents_company_date_range IS 'Optimiza consultas por empresa y rango de fechas';
COMMENT ON INDEX idx_documents_company_doc_type IS 'Optimiza consultas por tipo de documento y empresa';
COMMENT ON INDEX idx_documents_company_status_created IS 'Optimiza consultas por estado y empresa (más frecuente)';
COMMENT ON INDEX idx_documents_series_number IS 'Optimiza búsquedas por serie y número';
COMMENT ON INDEX idx_documents_cdc IS 'Optimiza búsquedas por CDC';
COMMENT ON INDEX idx_documents_track_id IS 'Optimiza búsquedas por track_id';
COMMENT ON INDEX idx_outbox_events_pending_processing IS 'Optimiza procesamiento de eventos pendientes (crítico)';
COMMENT ON INDEX idx_document_events_document_type IS 'Optimiza consultas de eventos por documento y tipo';
COMMENT ON INDEX idx_company_certificates_active IS 'Optimiza consultas de certificados activos';
COMMENT ON INDEX idx_company_timbrados_active IS 'Optimiza consultas de timbrados activos';
