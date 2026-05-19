## Sifenboot API

Integración Open Source para SIFEN (Paraguay).

Middleware de alto rendimiento basado en Spring Boot 3.4.0 (Virtual Threads) y Undertow.

###   Requisitos
Java 21 (Obligatorio para Virtual Threads)

PostgreSQL 15+

Maven Wrapper (incluido)

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

```bash
export DB_HOST=localhost
export DB_PORT=5432
export DB_NAME=sifenboot
export DB_USER=postgres
export DB_PASS=su_password
```

### Ejecutar configuración

```bash
mvn exec:java
```

Si no se encuentra `.env`, el sistema utilizará las variables del entorno y podrá solicitar la contraseña por consola.

### Inicialización de Base de Datos

El proyecto incluye un módulo de configuración independiente bajo el paquete `org.sifenboot.setup` para automatizar la creación de la base de datos, el aprovisionamiento de las tablas del sistema, el registro del usuario administrador inicial y la carga del diccionario geográfico de SIFEN (Departamentos, Distritos y Localidades).

#### Instrucciones de Ejecución (Vía Maven)

Al encontrarse el inicializador estructurado dentro del árbol de fuentes del proyecto, la ejecución debe realizarse a través del Maven Wrapper para asegurar la correcta resolución del classpath (Jackson, Driver de PostgreSQL y dependencias de seguridad).

Desde la raíz del proyecto, ejecute el siguiente comando según su sistema operativo:

#### Linux / macOS

```bash
./mvnw compile exec:java -Dexec.mainClass="org.sifenboot.setup.SetupDatabase"
```

#### En Windows (CMD):

```DOS
mvnw.cmd compile exec:java -Dexec.mainClass="org.sifenboot.setup.SetupDatabase"
```

>  Nota:
> Este proceso es interactivo y le solicitará ingresar la contraseña para el usuario administrador en la consola (presione Enter para usar `admin` por defecto).
>
> Asegúrese de que el archivo `src/main/java/org/sifenboot/setup/json/ubicaciones.json` esté presente antes de iniciar.




### Compilación y Ejecución
Una vez inicializada la base de datos, puede levantar el servicio siguiendo estos pasos:

Limpiar e instalar dependencias:

<pre><code>./mvnw clean install</code></pre>

### Ejecutar la aplicación:

<pre><code>./mvnw spring-boot:run</code></pre>

### Acceso al Panel:
Una vez que el servicio esté corriendo, puede acceder a la interfaz de administración en:

http://localhost:8080/login 

(Credenciales por defecto: admin / admin).


### Licencia
Apache 2.0