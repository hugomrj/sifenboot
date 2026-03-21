# Sifenboot API

Integracion Open Source para SIFEN (Paraguay).
Basado en Spring Boot 3.3.4 y Undertow.

## Requisitos
- Java 17
- PostgreSQL
- Maven Wrapper (incluido)

## Inicio Rapido

1. Configuracion:
   Ajustar credenciales de base de datos en:
   src/main/resources/application.yml

2. Compilar:
   ./mvnw clean install

3. Ejecutar:
   ./mvnw spring-boot:run

## Endpoints
- API Base: http://localhost:8080
- Swagger/OpenAPI: http://localhost:8080/swagger-ui.html

## Estructura
- src/main/java/org/sifenboot: Logica de negocio.
- src/main/resources/db/migration: Scripts Flyway.

Licencia: Apache 2.0