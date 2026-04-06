CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE,
    password TEXT
);

-- @password: admin
INSERT INTO usuarios (username, password)
VALUES ('admin', ':pass_admin')
ON CONFLICT (username) DO NOTHING;


-- Tabla de usuarios (ya la tienes)
CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE,
    password TEXT
);


-- 1. Tabla de Emisores (Datos Fiscales y de Negocio)
CREATE TABLE IF NOT EXISTS emisores (
    id SERIAL PRIMARY KEY,
    cod_emisor VARCHAR(50) UNIQUE NOT NULL, -- El alias para la URL
    ruc VARCHAR(20) UNIQUE NOT NULL,
    razon_social TEXT NOT NULL,
    nombre_fantasia TEXT,
    tipo_contribuyente INTEGER NOT NULL,

    -- Datos del Timbrado
    numero_timbrado VARCHAR(20) NOT NULL,
    inicio_vigencia_timbrado DATE NOT NULL,

    -- Ubicación y Contacto
    direccion TEXT NOT NULL,
    telefono VARCHAR(50),
    email VARCHAR(100),

    -- Actividad Económica
    actividad_economica_codigo INTEGER,
    actividad_economica_descripcion TEXT,

    creado_en TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- 2. Tabla de Certificados y Secretos (Configuración Sifen)
CREATE TABLE IF NOT EXISTS certificados (
    id SERIAL PRIMARY KEY,
    emisor_id INTEGER NOT NULL UNIQUE, -- Relación 1 a 1 con la tabla emisores

    -- Credenciales de Firma
    p12_contenido BYTEA NOT NULL,
    p12_password TEXT NOT NULL,

    -- Credenciales de API SSET
    ambiente VARCHAR(10) NOT NULL DEFAULT 'test',
    id_csc VARCHAR(10) NOT NULL,
    csc VARCHAR(100) NOT NULL,

    fecha_vencimiento_p12 DATE, -- Opcional: para avisar al cliente antes de que venza
    activo BOOLEAN DEFAULT TRUE,

    CONSTRAINT fk_emisor FOREIGN KEY (emisor_id) REFERENCES emisores(id) ON DELETE CASCADE,
    CONSTRAINT check_ambiente_cert CHECK (ambiente IN ('test', 'prod'))
);

-- Índices para optimizar las uniones (JOINs)
CREATE INDEX IF NOT EXISTS idx_emisores_cod ON emisores(cod_emisor);
CREATE INDEX IF NOT EXISTS idx_cert_emisor_id ON certificados(emisor_id);