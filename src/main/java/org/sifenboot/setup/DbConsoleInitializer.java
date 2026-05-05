package org.sifenboot.setup;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.*;
import java.util.List;
import java.util.Properties;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;




public class DbConsoleInitializer {

    public static void main(String[] args) {
        System.out.println(">> [INICIO] Iniciando proceso de configuración de Base de Datos...");

        Scanner consoleScanner = new Scanner(System.in);
        System.out.print(">> Ingrese la contraseña para el usuario administrador (Enter para usar 'admin'): ");
        String inputPass = consoleScanner.nextLine().trim();

        String finalPassword = inputPass.isEmpty() ? "admin" : inputPass;
        System.out.println(">> Se utilizará la contraseña: " + (inputPass.isEmpty() ? "admin (por defecto)" : "*******"));

        try {
            // Cargamos configuración desde .env o variables de sistema
            Properties props = loadEnvFile();

            // Leemos las variables estandarizadas (MAYÚSCULAS)
            String host = props.getProperty("DB_HOST", "localhost");
            String port = props.getProperty("DB_PORT", "5432");
            String dbName = props.getProperty("DB_NAME", "sifenboot");
            String user = props.getProperty("DB_USER", "postgres");
            String pass = props.getProperty("DB_PASS", "password");

            String baseUrl = "jdbc:postgresql://" + host + ":" + port + "/postgres";
            String targetUrl = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;

            ensureDatabaseExists(baseUrl, dbName, user, pass);

            // Cargamos el SQL inyectando la contraseña hasheada
            String sqlScript = loadSqlScript("db/setup.sql", finalPassword);

            if (!sqlScript.isEmpty()) {
                executeSqlScript(targetUrl, sqlScript, user, pass);
            } else {
                System.out.println(">> [ADVERTENCIA] El script SQL está vacío o no se encontró.");
            }

            System.out.println(">> [ÉXITO] Sistema inicializado correctamente.");

        } catch (Exception e) {
            System.err.println("\n>> [ERROR CRÍTICO]: Proceso interrumpido.");
            e.printStackTrace(); // Imprimimos el stack trace completo para depurar si falla
        }
    }

    // Método para leer .env manualmente (ya que esta clase corre fuera de Spring)
    private static Properties loadEnvFile() throws IOException {
        Properties props = new Properties();
        File envFile = new File(".env"); // Busca en la raíz del proyecto

        if (envFile.exists()) {
            System.out.println(">> [CONFIG] Cargando variables desde .env");
            List<String> lines = Files.readAllLines(envFile.toPath());
            for (String line : lines) {
                if (line.trim().isEmpty() || line.trim().startsWith("#")) {
                    continue;
                }
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    props.setProperty(parts[0].trim(), parts[1].trim());
                }
            }
        } else {
            System.out.println(">> [CONFIG] No se encontró .env, usando variables de entorno del sistema.");
        }

        // Sobrescribimos con variables del sistema si existen (ideal para Docker/CI)
        System.getenv().forEach(props::setProperty);

        return props;
    }

    // ELIMINÉ EL MÉTODO loadProperties ANTIGUO PORQUE YA NO SE USA

    private static void ensureDatabaseExists(String url, String dbName, String user, String pass) throws Exception {
        System.out.println(">> [DB] Verificando existencia de la base de datos '" + dbName + "'...");
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement()) {

            String checkSql = "SELECT 1 FROM pg_database WHERE datname = '" + dbName + "'";
            ResultSet rs = stmt.executeQuery(checkSql);

            if (!rs.next()) {
                System.out.println("   - La base de datos no existe. Creándola...");
                stmt.executeUpdate("CREATE DATABASE " + dbName);
                System.out.println("   - Base de datos '" + dbName + "' creada exitosamente.");
            } else {
                System.out.println("   - La base de datos ya existe. Continuando...");
            }
        }
    }

    private static String loadSqlScript(String path, String passwordToHash) throws Exception {
        System.out.println(">> [SQL] Leyendo script: " + path);
        try (InputStream is = DbConsoleInitializer.class.getClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                throw new RuntimeException("No se pudo encontrar el archivo SQL en: " + path);
            }
            Scanner s = new Scanner(is, StandardCharsets.UTF_8).useDelimiter("\\A");
            String content = s.hasNext() ? s.next() : "";

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashReal = encoder.encode(passwordToHash);

            if (content.contains(":pass_admin")) {
                content = content.replace(":pass_admin", hashReal);
                System.out.println("   - Hash de contraseña inyectado.");
            } else {
                System.out.println("   - [!] Advertencia: No se encontró marcador :pass_admin.");
            }

            return content;
        }
    }

    private static void executeSqlScript(String url, String script, String user, String pass) throws Exception {
        System.out.println(">> [SQL] Ejecutando script en base de datos destino...");
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement()) {
            stmt.execute(script);
            System.out.println("   - Script ejecutado correctamente.");
        } catch (SQLException e) {
            throw new RuntimeException("Error al ejecutar el SQL: " + e.getMessage(), e);
        }
    }
}