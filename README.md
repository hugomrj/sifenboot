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
El proyecto incluye un script lanzador independiente para automatizar la creación de la base de datos, las tablas y el usuario administrador inicial (admin / admin123).

### Ejecución rápida (Recomendado)
Desde la raíz del proyecto, ejecute el siguiente comando:

<pre><code>java SetupDatabase.java</code></pre>

Nota: Este comando utiliza las capacidades de Java 21 para ejecutar código fuente directamente. No requiere compilación previa ni ensucia el proyecto con archivos .class.

### Ejecución vía Maven (Alternativa)
Si prefiere utilizar el ciclo de vida de Maven directamente:

<pre><code>./mvnw compile exec:java -Dexec.mainClass="org.sifenboot.setup.DbConsoleInitializer"</code></pre>

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