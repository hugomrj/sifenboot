-- Usuarios del sistema
CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE,
    password TEXT
);

-- @password: admin
INSERT INTO usuarios (username, password)
VALUES ('admin', ':pass_admin')
ON CONFLICT (username) DO NOTHING;


-- 1. Departamentos (Ya la tienes, pero aseguramos integridad)
CREATE TABLE IF NOT EXISTS departamentos (
    id INTEGER PRIMARY KEY,
    descripcion VARCHAR(100) NOT NULL
);

-- 2. Distritos
CREATE TABLE IF NOT EXISTS distritos (
    id INTEGER PRIMARY KEY,
    departamento_id INTEGER NOT NULL,
    descripcion VARCHAR(100) NOT NULL,
    CONSTRAINT fk_departamento FOREIGN KEY (departamento_id) REFERENCES departamentos(id) ON DELETE CASCADE
);

-- 3. Localidades
-- Usamos un SERIAL o una PK compuesta porque los códigos de localidad
-- a veces se repiten entre distritos en los documentos de SET/SIFEN
CREATE TABLE IF NOT EXISTS localidades (
    id SERIAL PRIMARY KEY,
    distrito_id INTEGER NOT NULL,
    codigo_localidad INTEGER NOT NULL,
    descripcion VARCHAR(100) NOT NULL,
    CONSTRAINT fk_distrito FOREIGN KEY (distrito_id) REFERENCES distritos(id) ON DELETE CASCADE
);



-- Emisores (Datos de negocio)
-- 4. Emisores (Actualizado con relaciones)
CREATE TABLE IF NOT EXISTS emisores (
    id SERIAL PRIMARY KEY,
    cod_emisor VARCHAR(50) UNIQUE NOT NULL,
    ruc VARCHAR(20) UNIQUE NOT NULL,
    ruc_dv INTEGER NOT NULL,
    razon_social TEXT NOT NULL,
    nombre_fantasia TEXT,
    tipo_contribuyente INTEGER NOT NULL,
    numero_timbrado VARCHAR(20) NOT NULL,
    fecha_inicio_timbrado DATE NOT NULL,
    direccion TEXT NOT NULL,
    numero_casa INTEGER DEFAULT 0,
    departamento_id INTEGER,
    distrito_id INTEGER,
    localidad_id INTEGER,
    telefono VARCHAR(50),
    email VARCHAR(100),
    actividad_economica_codigo INTEGER,
    actividad_economica_descripcion TEXT,
    creado_en TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_emisor_depto FOREIGN KEY (departamento_id) REFERENCES departamentos(id),
    CONSTRAINT fk_emisor_distrito FOREIGN KEY (distrito_id) REFERENCES distritos(id),
    CONSTRAINT fk_emisor_localidad FOREIGN KEY (localidad_id) REFERENCES localidades(id)
);



-- Configuración SIFEN
CREATE TABLE IF NOT EXISTS emisores_configuraciones (
    id SERIAL PRIMARY KEY,
    emisor_id INTEGER NOT NULL UNIQUE,
    ambiente VARCHAR(10) NOT NULL DEFAULT 'test',
    id_csc VARCHAR(10) NOT NULL,
    csc VARCHAR(100) NOT NULL,
    api_token VARCHAR(255),

    CONSTRAINT fk_emisor_config FOREIGN KEY (emisor_id) REFERENCES emisores(id) ON DELETE CASCADE,
    CONSTRAINT check_ambiente_config CHECK (ambiente IN ('test', 'prod'))
);

-- Certificados
CREATE TABLE IF NOT EXISTS certificados (
    id SERIAL PRIMARY KEY,
    emisor_id INTEGER NOT NULL UNIQUE,
    p12_contenido BYTEA NOT NULL,
    p12_password TEXT NOT NULL,
    fecha_vencimiento_p12 DATE,
    activo BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_emisor_cert FOREIGN KEY (emisor_id) REFERENCES emisores(id) ON DELETE CASCADE
);

-- ESTADOS DE DOCUMENTO (Global)
CREATE TABLE IF NOT EXISTS estados_documento (
    id SMALLINT PRIMARY KEY,
    codigo VARCHAR(25) UNIQUE NOT NULL,
    descripcion VARCHAR(100) NOT NULL
);

-- Inserts de Estados Iniciales
INSERT INTO estados_documento (id, codigo, descripcion) VALUES
    (1, 'RECIBIDO', 'JSON recibido y transformado a XML localmente'),
    (2, 'ENVIADO_A_SET', 'Lote enviado a SIFEN, esperando procesamiento'),
    (3, 'APROBADO', 'Documento aprobado por la SET, CDC generado'),
    (4, 'RECHAZADO', 'Rechazado por la SET (error de validación)'),
    (5, 'ERROR_CONEXION', 'Falló el envío por timeout o caída de SIFEN'),
    (6, 'CANCELADO', 'Documento anulado por nota de crédito')
ON CONFLICT (id) DO NOTHING;

-- Índices globales
CREATE INDEX IF NOT EXISTS idx_emisores_cod ON emisores(cod_emisor);
CREATE INDEX IF NOT EXISTS idx_emisor_config_emisor_id ON emisores_configuraciones(emisor_id);
CREATE INDEX IF NOT EXISTS idx_cert_emisor_id ON certificados(emisor_id);




