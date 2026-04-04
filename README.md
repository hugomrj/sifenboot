# Sifenboot API

Integracion Open Source para SIFEN (Paraguay).
Middleware de alto rendimiento basado en Spring Boot 3.4.0 y Undertow.

## Requisitos
- Java 21 (Virtual Threads)
- PostgreSQL 15+
- Maven Wrapper (incluido)

## Configuracion Previa

Antes de ejecutar, asegurese de que el archivo src/main/resources/database.properties
tenga las credenciales correctas de su instancia de PostgreSQL:

db.host=localhost
db.port=5432
db.name=sifenboot
db.user=postgres
db.pass=su_password

## Inicializacion de Base de Datos

El proyecto incluye una utilidad para crear la base de datos, las tablas y el
usuario administrador inicial (admin/admin123) de forma automatica.

Ejecute el siguiente comando desde la raiz del proyecto:

mvn compile exec:java -Dexec.mainClass="org.sifenboot.setup.DbConsoleInitializer"

Nota: Si prefiere la instalacion manual, el script SQL completo se encuentra en:
src/main/resources/db/setup.sql

## Compilacion y Ejecucion

Una vez inicializada la base de datos, puede levantar el servicio:

1. Limpiar y compilar:
<pre><code>./mvnw clean install
./mvnw clean install
</code></pre>

2. Ejecutar la aplicacion:
   ./mvnw spring-boot:run

## Arquitectura del Proyecto

El sistema utiliza una separacion de dominios para mantener el codigo limpio:

- core: Motor transaccional. Maneja XML, Firma Digital, comunicacion SOAP y CDC.
- panel: Administracion (Dashboard). Gestion de Usuarios, Empresas y Workers.
- common: Configuraciones transversales, Seguridad (JWT) y Manejo de Errores.
- worker: Procesos asincronos para el envio masivo de lotes a SIFEN.

## Endpoints de Interes

- API Core: http://localhost:8080/api/v1/core
- API Panel: http://localhost:8080/api/v1/panel
- Swagger UI: http://localhost:8080/swagger-ui.html
- Consola H2 (si aplica): http://localhost:8080/h2-console

## Licencia
Apache 2.0