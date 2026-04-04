# Sifenboot API

Integracion Open Source para SIFEN (Paraguay).
Middleware de alto rendimiento basado en Spring Boot 3.4.0 y Undertow.

## Requisitos
- Java 21 (Virtual Threads)
- PostgreSQL 15+
- Maven Wrapper (incluido)

## Inicio Rapido

1. Configuracion de Base de Datos:
   Ajustar credenciales en: src/main/resources/database.properties

2. Inicializacion Automatica (Recomendado):
   Este comando crea la base de datos, las tablas y el usuario administrador inicial.

   mvn compile exec:java -Dexec.mainClass="org.sifenboot.setup.DbConsoleInitializer"

   Nota: El archivo src/main/resources/db/setup.sql esta disponible si prefieres
   realizar la creacion de tablas manualmente.

3. Compilar:
   ./mvnw clean install

4. Ejecutar:
   ./mvnw spring-boot:run

## Arquitectura
El sistema se divide en dominios claros para separar la logica de negocio de la gestion:

- core: Motor transaccional (XML, Firma Digital, SOAP, CDC).
- panel: Administracion del sistema (Usuarios, Empresas, Monitoreo de Workers).
- common: Configuraciones globales, seguridad y manejo de excepciones.
- worker: Procesos asincronos para envio de lotes y sincronizacion.

## Endpoints
- API Base: http://localhost:8080/api/v1
- Swagger/OpenAPI: http://localhost:8080/swagger-ui.html
- Web Admin: http://localhost:8080/admin.html

## Licencia
Apache 2.0