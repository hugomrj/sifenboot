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


-- 1. Tabla de Departamentos
CREATE TABLE departamentos (
    id INTEGER PRIMARY KEY,
    descripcion VARCHAR(50) NOT NULL UNIQUE
);
INSERT INTO departamentos (id, descripcion) VALUES
(1, 'CAPITAL'),
(2, 'CONCEPCION'),
(3, 'SAN PEDRO'),
(4, 'CORDILLERA'),
(5, 'GUAIRA'),
(6, 'CAAGUAZU'),
(7, 'CAAZAPA'),
(8, 'ITAPUA'),
(9, 'MISIONES'),
(10, 'PARAGUARI'),
(11, 'ALTO PARANA'),
(12, 'CENTRAL'),
(13, 'NEEMBUCU'),
(14, 'AMAMBAY'),
(15, 'CANINDEYU'),
(16, 'PRESIDENTE HAYES'),
(17, 'BOQUERON'),
(18, 'ALTO PARAGUAY');




-- 2. Tabla de Emisores Sincronizada con el Modelo Java
-- 2. Tabla de Emisores (Corregida para JPA)
CREATE TABLE emisores (
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

    -- CAMBIO AQUÍ: Debe ser departamento_id para coincidir con @JoinColumn o el default de JPA
    departamento_id INTEGER,

    telefono VARCHAR(50),
    email VARCHAR(100),
    actividad_economica_codigo INTEGER,
    actividad_economica_descripcion TEXT,

    -- Configuración SIFEN
    ambiente VARCHAR(10) NOT NULL DEFAULT 'test',
    id_csc VARCHAR(10) NOT NULL,
    csc VARCHAR(100) NOT NULL,

    creado_en TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    -- Validaciones y FK
    CONSTRAINT fk_departamento FOREIGN KEY (departamento_id) REFERENCES departamentos(id),
    CONSTRAINT check_ambiente_emisor CHECK (ambiente IN ('test', 'prod')),
    CONSTRAINT check_ruc_dv CHECK (ruc_dv >= 0 AND ruc_dv <= 9)
);




-- 3. Tabla de Certificados (Solo binario y password)
CREATE TABLE certificados (
    id SERIAL PRIMARY KEY,
    emisor_id INTEGER NOT NULL UNIQUE,
    p12_contenido BYTEA NOT NULL,
    p12_password TEXT NOT NULL,
    fecha_vencimiento_p12 DATE,
    activo BOOLEAN DEFAULT TRUE,
    CONSTRAINT fk_emisor FOREIGN KEY (emisor_id) REFERENCES emisores(id) ON DELETE CASCADE
);

-- 4. Índices
CREATE INDEX idx_emisores_cod ON emisores(cod_emisor);
CREATE INDEX idx_cert_emisor_id ON certificados(emisor_id);