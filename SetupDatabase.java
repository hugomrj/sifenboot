import java.io.File;
import java.io.IOException;



/**
 * Utility to initialize the Sifenboot Database environment.
 * This class triggers the Maven lifecycle to compile and execute
 * the DbConsoleInitializer.
 */
public class SetupDatabase {

    public static void main(String[] args) {
        // Detectar el sistema operativo para invocar el ejecutable correcto de Maven
        String os = System.getProperty("os.name").toLowerCase();
        String mavenCmd = os.contains("win") ? "mvn.cmd" : "mvn";

        System.out.println("====================================================");
        System.out.println("   SIFENBOOT - DATABASE SETUP UTILITY");
        System.out.println("====================================================");
        System.out.println(">> Iniciando comando: " + mavenCmd + " compile exec:java...");
        System.out.println(">> Por favor, espere mientras se procesa el script SQL...");
        System.out.println("----------------------------------------------------");

        try {
            /*
            mvn compile exec:java -Dexec.mainClass="org.sifenboot.setup.DbConsoleInitializer"
            */

            // Configurar el proceso para ejecutar el mainClass específico
            ProcessBuilder pb = new ProcessBuilder(
                    mavenCmd,
                    "compile",
                    "exec:java",
                    "-Dexec.mainClass=org.sifenboot.setup.DbConsoleInitializer"
            );


            // Establecer el directorio de trabajo en la raíz (donde está el pom.xml)
            pb.directory(new File(System.getProperty("user.dir")));

            // Redirigir la entrada/salida para ver el log de DbConsoleInitializer en vivo
            pb.inheritIO();

            Process process = pb.start();

            // Esperar a que el proceso termine y capturar el código de salida
            int exitCode = process.waitFor();

            System.out.println("----------------------------------------------------");
            if (exitCode == 0) {
                System.out.println("[SUCCESS] La base de datos se ha configurado correctamente.");
            } else {
                System.err.println("[ERROR] El proceso terminó con errores (Código: " + exitCode + ").");
                System.err.println("Asegúrate de que PostgreSQL esté corriendo y el archivo pom.xml sea válido.");
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("[CRITICAL ERROR] No se pudo ejecutar el comando Maven.");
            System.err.println("Detalle: " + e.getMessage());
            System.err.println("Verifica que 'mvn' esté configurado en las variables de entorno (PATH).");
        }

        System.out.println("====================================================");
    }
}
