-- SIFEN Official Catalogs Tables
-- Tablas para catálogos oficiales SIFEN v150

-- Tabla de países receptores (ISO 3166-1)
CREATE TABLE IF NOT EXISTS pais_receptor (
    codigo VARCHAR(3) PRIMARY KEY,
    pais VARCHAR(100) NOT NULL,
    codigo_iso VARCHAR(3) NOT NULL,
    activo BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de actividades económicas
CREATE TABLE IF NOT EXISTS actividad_economica (
    codigo VARCHAR(10) PRIMARY KEY,
    descripcion VARCHAR(200) NOT NULL,
    categoria VARCHAR(100),
    activo BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de motivos de crédito
CREATE TABLE IF NOT EXISTS motivo_credito (
    codigo VARCHAR(10) PRIMARY KEY,
    descripcion VARCHAR(200) NOT NULL,
    activo BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de tipos de documento de identidad
CREATE TABLE IF NOT EXISTS tipo_documento_identidad (
    codigo VARCHAR(10) PRIMARY KEY,
    descripcion VARCHAR(100) NOT NULL,
    activo BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de responsables de emisión
CREATE TABLE IF NOT EXISTS responsable_emision (
    codigo VARCHAR(10) PRIMARY KEY,
    descripcion VARCHAR(200) NOT NULL,
    activo BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de motivos de emisión
CREATE TABLE IF NOT EXISTS motivo_emision (
    codigo VARCHAR(10) PRIMARY KEY,
    descripcion VARCHAR(200) NOT NULL,
    activo BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de denominaciones de tarjeta
CREATE TABLE IF NOT EXISTS denominacion_tarjeta (
    codigo VARCHAR(10) PRIMARY KEY,
    descripcion VARCHAR(100) NOT NULL,
    activo BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de tipos de IVA
CREATE TABLE IF NOT EXISTS tipo_iva (
    codigo VARCHAR(10) PRIMARY KEY,
    descripcion VARCHAR(100) NOT NULL,
    porcentaje DECIMAL(5,2) NOT NULL,
    activo BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de tipos de operación
CREATE TABLE IF NOT EXISTS tipo_operacion (
    codigo VARCHAR(10) PRIMARY KEY,
    descripcion VARCHAR(200) NOT NULL,
    activo BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de tipos de documento electrónico (actualizar estructura existente)
ALTER TABLE tipo_documento_electronico ADD COLUMN IF NOT EXISTS activo BOOLEAN DEFAULT true;
ALTER TABLE tipo_documento_electronico ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE tipo_documento_electronico ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Tabla de monedas (actualizar estructura existente)
ALTER TABLE moneda ADD COLUMN IF NOT EXISTS descripcion VARCHAR(200);
ALTER TABLE moneda ADD COLUMN IF NOT EXISTS activo BOOLEAN DEFAULT true;
ALTER TABLE moneda ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE moneda ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Tabla de unidades de medida (actualizar estructura existente)
ALTER TABLE unidad_medida ADD COLUMN IF NOT EXISTS descripcion VARCHAR(200);
ALTER TABLE unidad_medida ADD COLUMN IF NOT EXISTS activo BOOLEAN DEFAULT true;
ALTER TABLE unidad_medida ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE unidad_medida ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Tabla de métodos de pago (actualizar estructura existente)
ALTER TABLE metodo_pago ADD COLUMN IF NOT EXISTS descripcion VARCHAR(200);
ALTER TABLE metodo_pago ADD COLUMN IF NOT EXISTS activo BOOLEAN DEFAULT true;
ALTER TABLE metodo_pago ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE metodo_pago ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Tabla de tipos de transacción (actualizar estructura existente)
ALTER TABLE tipo_transaccion ADD COLUMN IF NOT EXISTS activo BOOLEAN DEFAULT true;
ALTER TABLE tipo_transaccion ADD COLUMN IF NOT EXISTS created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;
ALTER TABLE tipo_transaccion ADD COLUMN IF NOT EXISTS updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

-- Insertar datos iniciales de países (ISO 3166-1)
INSERT INTO pais_receptor (codigo, pais, codigo_iso) VALUES
('600', 'Paraguay', 'PRY'),
('032', 'Argentina', 'ARG'),
('068', 'Bolivia', 'BOL'),
('076', 'Brasil', 'BRA'),
('152', 'Chile', 'CHL'),
('170', 'Colombia', 'COL'),
('218', 'Ecuador', 'ECU'),
('320', 'Guatemala', 'GTM'),
('340', 'Honduras', 'HND'),
('484', 'México', 'MEX'),
('558', 'Nicaragua', 'NIC'),
('604', 'Perú', 'PER'),
('858', 'Uruguay', 'URY'),
('840', 'Estados Unidos', 'USA'),
('724', 'España', 'ESP'),
('826', 'Reino Unido', 'GBR'),
('276', 'Alemania', 'DEU'),
('250', 'Francia', 'FRA'),
('380', 'Italia', 'ITA'),
('756', 'Suiza', 'CHE');

-- Insertar datos iniciales de actividades económicas
INSERT INTO actividad_economica (codigo, descripcion, categoria) VALUES
('0111', 'Cultivo de cereales (excepto arroz), legumbres y semillas oleaginosas', 'Agricultura'),
('0112', 'Cultivo de arroz', 'Agricultura'),
('0113', 'Cultivo de hortalizas, raíces y tubérculos', 'Agricultura'),
('0114', 'Cultivo de caña de azúcar', 'Agricultura'),
('0115', 'Cultivo de tabaco', 'Agricultura'),
('0116', 'Cultivo de plantas para fibras textiles', 'Agricultura'),
('0119', 'Otros cultivos no perennes', 'Agricultura'),
('0121', 'Cultivo de uvas', 'Agricultura'),
('0122', 'Cultivo de frutas tropicales y subtropicales', 'Agricultura'),
('0123', 'Cultivo de cítricos', 'Agricultura'),
('0124', 'Cultivo de frutas de pepita y de hueso', 'Agricultura'),
('0125', 'Cultivo de otros árboles frutales y frutos secos', 'Agricultura'),
('0126', 'Cultivo de frutos oleaginosos', 'Agricultura'),
('0127', 'Cultivo de plantas para bebidas', 'Agricultura'),
('0128', 'Cultivo de especias, plantas aromáticas, medicinales y farmacéuticas', 'Agricultura'),
('0129', 'Otros cultivos perennes', 'Agricultura'),
('0130', 'Propagación de plantas', 'Agricultura'),
('0141', 'Cría de ganado bovino y búfalos', 'Ganadería'),
('0142', 'Cría de caballos y otros equinos', 'Ganadería'),
('0143', 'Cría de camellos y otros camélidos', 'Ganadería'),
('0144', 'Cría de ovejas y cabras', 'Ganadería'),
('0145', 'Cría de cerdos', 'Ganadería'),
('0146', 'Cría de aves de corral', 'Ganadería'),
('0149', 'Cría de otros animales', 'Ganadería'),
('0150', 'Producción mixta', 'Ganadería'),
('0161', 'Actividades de apoyo a la agricultura', 'Servicios Agrícolas'),
('0162', 'Actividades de apoyo a la ganadería', 'Servicios Agrícolas'),
('0163', 'Actividades posteriores a la cosecha', 'Servicios Agrícolas'),
('0164', 'Tratamiento de semillas para propagación', 'Servicios Agrícolas'),
('0170', 'Caza, captura y servicios conexos', 'Caza y Pesca'),
('0210', 'Silvicultura y otras actividades forestales', 'Silvicultura'),
('0220', 'Extracción de madera', 'Silvicultura'),
('0230', 'Recolección de productos forestales no madereros', 'Silvicultura'),
('0240', 'Servicios de apoyo a la silvicultura', 'Silvicultura'),
('0311', 'Pesca marina', 'Pesca'),
('0312', 'Pesca de agua dulce', 'Pesca'),
('0321', 'Acuicultura marina', 'Acuicultura'),
('0322', 'Acuicultura de agua dulce', 'Acuicultura'),
('0510', 'Extracción de carbón', 'Minería'),
('0520', 'Extracción de lignito', 'Minería'),
('0610', 'Extracción de petróleo crudo', 'Minería'),
('0620', 'Extracción de gas natural', 'Minería'),
('0710', 'Extracción de minerales de hierro', 'Minería'),
('0721', 'Extracción de minerales de uranio y torio', 'Minería'),
('0729', 'Extracción de otros minerales metalíferos no ferrosos', 'Minería'),
('0811', 'Extracción de piedra, arena y arcilla', 'Minería'),
('0812', 'Extracción de grava y arena', 'Minería'),
('0813', 'Extracción de arcilla y caolín', 'Minería'),
('0891', 'Extracción de minerales para productos químicos y fertilizantes', 'Minería'),
('0892', 'Extracción de turba', 'Minería'),
('0893', 'Extracción de sal', 'Minería'),
('0899', 'Extracción de otros minerales no metalíferos', 'Minería'),
('0910', 'Actividades de apoyo a la extracción de petróleo y gas natural', 'Minería'),
('0990', 'Actividades de apoyo a la extracción de otros minerales', 'Minería');

-- Insertar datos iniciales de motivos de crédito
INSERT INTO motivo_credito (codigo, descripcion) VALUES
('01', 'Venta a crédito de bienes'),
('02', 'Venta a crédito de servicios'),
('03', 'Venta a crédito de bienes y servicios'),
('04', 'Anticipo de pago'),
('05', 'Préstamo'),
('06', 'Leasing'),
('07', 'Factoring'),
('08', 'Otros motivos de crédito');

-- Insertar datos iniciales de tipos de documento de identidad
INSERT INTO tipo_documento_identidad (codigo, descripcion) VALUES
('1', 'Cédula de Identidad'),
('2', 'Pasaporte'),
('3', 'Documento de Identidad Extranjero'),
('4', 'RUC'),
('5', 'Cédula de Identidad Paraguaya'),
('6', 'Documento de Identidad Mercosur'),
('7', 'Documento de Identidad Andino'),
('8', 'Documento de Identidad Centroamericano'),
('9', 'Otro');

-- Insertar datos iniciales de responsables de emisión
INSERT INTO responsable_emision (codigo, descripcion) VALUES
('1', 'Emisor'),
('2', 'Receptor'),
('3', 'Tercero'),
('4', 'Otro');

-- Insertar datos iniciales de motivos de emisión
INSERT INTO motivo_emision (codigo, descripcion) VALUES
('1', 'Venta'),
('2', 'Prestación de servicios'),
('3', 'Mixto'),
('4', 'Otro');

-- Insertar datos iniciales de denominaciones de tarjeta
INSERT INTO denominacion_tarjeta (codigo, descripcion) VALUES
('01', 'Visa'),
('02', 'Mastercard'),
('03', 'American Express'),
('04', 'Diners Club'),
('05', 'JCB'),
('06', 'Discover'),
('07', 'Maestro'),
('08', 'Electron'),
('09', 'Otra');

-- Insertar datos iniciales de tipos de IVA
INSERT INTO tipo_iva (codigo, descripcion, porcentaje) VALUES
('0', 'Exento', 0.00),
('5', '5%', 5.00),
('10', '10%', 10.00),
('15', '15%', 15.00);

-- Insertar datos iniciales de tipos de operación
INSERT INTO tipo_operacion (codigo, descripcion) VALUES
('1', 'Venta'),
('2', 'Compra'),
('3', 'Consumo'),
('4', 'Donación'),
('5', 'Exportación'),
('6', 'Importación'),
('7', 'Otro');

-- Crear índices para mejorar el rendimiento
CREATE INDEX IF NOT EXISTS idx_pais_receptor_codigo ON pais_receptor(codigo);
CREATE INDEX IF NOT EXISTS idx_actividad_economica_codigo ON actividad_economica(codigo);
CREATE INDEX IF NOT EXISTS idx_motivo_credito_codigo ON motivo_credito(codigo);
CREATE INDEX IF NOT EXISTS idx_tipo_documento_identidad_codigo ON tipo_documento_identidad(codigo);
CREATE INDEX IF NOT EXISTS idx_responsable_emision_codigo ON responsable_emision(codigo);
CREATE INDEX IF NOT EXISTS idx_motivo_emision_codigo ON motivo_emision(codigo);
CREATE INDEX IF NOT EXISTS idx_denominacion_tarjeta_codigo ON denominacion_tarjeta(codigo);
CREATE INDEX IF NOT EXISTS idx_tipo_iva_codigo ON tipo_iva(codigo);
CREATE INDEX IF NOT EXISTS idx_tipo_operacion_codigo ON tipo_operacion(codigo);
