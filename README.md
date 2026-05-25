# Sifenboot

Integración Open Source para SIFEN (Paraguay).

Middleware de alto rendimiento basado en Spring Boot 3.4.0 (Virtual Threads) y Undertow.

Para ver la documentación completa en línea, visita: [hugomrj.github.io/sifenboot/api.html](https://hugomrj.github.io/sifenboot/api.html)

## Requisitos

- Java 21 (Obligatorio para Virtual Threads)
- PostgreSQL 15+
- Maven Wrapper (incluido)

---

## Configuración previa

Antes de iniciar, configure las credenciales de PostgreSQL.

El proceso de inicialización buscará la configuración en este orden:

1. Archivo `.env` (recomendado)
2. Variables de entorno del sistema
3. Solicitud interactiva por consola (solo contraseña)

### Opción 1 — Archivo `.env`

Crear un archivo `.env` en la raíz del proyecto:

```env
DB_HOST=localhost
DB_PORT=5432
DB_NAME=sifenboot
DB_USER=postgres
DB_PASS=su_password
```

### Opción 2 — Variables de entorno

Linux / macOS

```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=sifenboot
export DB_USER=postgres
export DB_PASS=su_password
```

Windows (PowerShell)

```powershell
$env:DB_HOST="localhost"
$env:DB_PORT="5432"
$env:DB_NAME="sifenboot"
$env:DB_USER="postgres"
$env:DB_PASS="su_password"
```

Si no se encuentra `.env`, el sistema utilizará las variables del entorno y podrá solicitar la contraseña por consola.

---

## Inicialización de Base de Datos

El proyecto incluye un módulo de configuración independiente bajo el paquete:

```txt
org.sifenboot.setup
```

Este proceso automatiza:

- Creación de la base de datos
- Aprovisionamiento de tablas del sistema
- Registro del usuario administrador inicial
- Carga del diccionario geográfico de SIFEN (Departamentos, Distritos y Localidades)

### Ejecutar configuración

#### Linux / macOS

```bash
./mvnw compile exec:java -Dexec.mainClass="org.sifenboot.setup.SetupDatabase"
```

#### Windows (CMD / PowerShell)

```cmd
.\mvnw.cmd compile exec:java "-Dexec.mainClass=org.sifenboot.setup.SetupDatabase"
```

> Nota:
>
> Este proceso es interactivo y solicitará ingresar la contraseña del usuario administrador.
>
> Presione Enter para utilizar `admin` como contraseña por defecto.
>
> Asegúrese de que exista el archivo:
>
> `src/main/java/org/sifenboot/setup/json/ubicaciones.json`

---


## Compilación

### Linux / macOS

```bash
./mvnw clean install
```

### Windows

```cmd
.\mvnw.cmd clean install
```

---

## Ejecutar aplicación (Modo Desarrollo)

### Linux / macOS

```bash
./mvnw spring-boot:run
```

### Windows

```cmd
.\mvnw.cmd spring-boot:run
```

---

## Ejecutar aplicación (Modo Producción)

Generar el artefacto:

### Linux / macOS

```bash
./mvnw clean package
```

### Windows

```cmd
.\mvnw.cmd clean package
```

Esto generará:

```txt
target/sifenboot-api-1.0.0-SNAPSHOT.jar
```

Ejecutar el JAR:

### Linux / macOS

```bash
java -jar target/sifenboot-api-1.0.0-SNAPSHOT.jar
```

### Windows

```cmd
java -jar target\sifenboot-api-1.0.0-SNAPSHOT.jar
```

---

## Acceso al Panel

Una vez iniciado el servicio:

```txt
http://localhost:8080/login
```

Credenciales por defecto:

```txt
Usuario: admin
Contraseña: tu_constraseña
```

---

## Licencia

MIT License