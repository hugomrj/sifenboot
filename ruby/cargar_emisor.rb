require 'pg'
require 'dotenv/load'

conn = PG.connect(
  host: ENV['DB_HOST'],
  port: ENV['DB_PORT'],
  dbname: ENV['DB_NAME'],
  user: ENV['DB_USER'],
  password: ENV['DB_PASS']
)

conn.exec <<-SQL
INSERT INTO emisores (
    cod_emisor,
    ruc,
    ruc_dv,
    razon_social,
    nombre_fantasia,
    tipo_contribuyente,
    numero_timbrado,
    fecha_inicio_timbrado,
    direccion,
    numero_casa,
    departamento_id,
    telefono,
    email,
    actividad_economica_codigo,
    actividad_economica_descripcion
) VALUES (
    'sanisidro',
    '769125',
    4,
    'JUAN CARLOS SOTO MARTINEZ',
    'Ferreteria San Isidro',
    1,
    '18717477',
    '2026-03-12',
    'TUYUTI C/ RUTA PY08 N° 0',
    0,
    6,
    '0521202341',
    'lucia_sanchez@hotmail.com',
    47521,
    'COMERCIO AL POR MENOR DE ARTÍCULOS DE FERRETERÍA'
);
SQL



conn.exec <<-SQL
INSERT INTO public.emisores_configuraciones (
    emisor_id,
    ambiente,
    id_csc,
    csc,
    api_token
) VALUES (
    1,
    'prod',
    '0002',
    '2E4419126d1f31f3644dcD21e523d9d0',
    'fidelio'
);
SQL





puts "Registro insertado"





