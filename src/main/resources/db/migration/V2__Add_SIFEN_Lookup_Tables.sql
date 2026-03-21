-- SIFEN Lookup Tables
-- Tablas de referencia para el sistema SIFEN

-- Tabla de métodos de pago
CREATE TABLE metodo_pago (
    codigo VARCHAR(10) PRIMARY KEY,
    metodo VARCHAR(100) NOT NULL
);

-- Tabla de unidades de medida
CREATE TABLE unidad_medida (
    codigo VARCHAR(10) PRIMARY KEY,
    unidad VARCHAR(100) NOT NULL
);

-- Tabla de tipos de transacción
CREATE TABLE tipo_transaccion (
    codigo VARCHAR(10) PRIMARY KEY,
    descripcion VARCHAR(200) NOT NULL
);

-- Tabla de monedas
CREATE TABLE moneda (
    codigoISO VARCHAR(3) PRIMARY KEY,
    moneda VARCHAR(100) NOT NULL,
    simbolo VARCHAR(10) NOT NULL
);

-- Insertar datos de métodos de pago
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
('99', 'Otro');

-- Insertar datos de unidades de medida
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
('660', 'Metro lineal (ml)');

-- Insertar datos de tipos de transacción
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
('13', 'Muestras médicas (Art. 3 RG 24/2014)');

-- Insertar datos de monedas
INSERT INTO moneda (codigoISO, moneda, simbolo) VALUES
('USD', 'Dólar estadounidense', '$'),
('EUR', 'Euro', '€'),
('PYG', 'Guaraní paraguayo', '₲'),
('ARS', 'Peso argentino', '$'),
('BRL', 'Real brasileño', 'R$');

