## Sifenboot API

Integración Open Source para SIFEN (Paraguay).

Middleware de alto rendimiento basado en Spring Boot 3.4.0 (Virtual Threads) y Undertow.

###   Requisitos
Java 21 (Obligatorio para Virtual Threads)

PostgreSQL 15+

Maven Wrapper (incluido)

### Configuración Previa
Antes de iniciar, asegúrese de que el archivo src/main/resources/database.properties contenga las credenciales correctas de su instancia de PostgreSQL:

#### /src/resources/database.properties
<pre><code>db.host=localhost
db.port=5432
db.name=sifenboot
db.user=postgres
db.pass=su_password
</code></pre>





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