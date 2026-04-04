package org.sifenboot.setup;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class DbConsoleInitializer {

    public static void main(String[] args) {
        System.out.println(">> [INICIO] Iniciando proceso de configuración de Base de Datos...");

        try {
            // 1. Cargar configuración
            Properties props = loadProperties();

            String host = props.getProperty("db.host", "localhost");
            String port = props.getProperty("db.port", "5432");
            String dbName = props.getProperty("db.name", "sifenboot");
            String user = props.getProperty("db.user", "postgres");
            String pass = props.getProperty("db.pass", "password");

            String baseUrl = "jdbc:postgresql://" + host + ":" + port + "/postgres";
            String targetUrl = "jdbc:postgresql://" + host + ":" + port + "/" + dbName;

            // 2. Crear la Base de Datos si no existe
            ensureDatabaseExists(baseUrl, dbName, user, pass);

            // 3. Leer el Script SQL
            String sqlScript = loadSqlScript("db/setup.sql");

            // 4. Ejecutar el Script en la base de datos destino
            if (!sqlScript.isEmpty()) {
                executeSqlScript(targetUrl, sqlScript, user, pass);
            } else {
                System.out.println(">> [ADVERTENCIA] El script SQL está vacío o no se encontró.");
            }

            System.out.println(">> [ÉXITO] Sistema inicializado correctamente.");

        } catch (Exception e) {
            System.err.println("\n>> [ERROR CRÍTICO]: Proceso interrumpido.");
            System.err.println(">> Detalle: " + e.getMessage());
            // Imprimimos la pila solo si necesitas ver la línea exacta del error
            // e.printStackTrace();
        }
    }

    private static Properties loadProperties() throws Exception {
        System.out.println(">> [1/4] Cargando database.properties...");
        Properties props = new Properties();
        try (InputStream input = DbConsoleInitializer.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                throw new RuntimeException("No se encontró el archivo 'database.properties' en resources.");
            }
            props.load(input);
            System.out.println("   - Configuración cargada.");
            return props;
        }
    }

    private static void ensureDatabaseExists(String url, String dbName, String user, String pass) throws Exception {
        System.out.println(">> [2/4] Verificando existencia de la base de datos '" + dbName + "'...");

        // Aquí corregimos el error del executeUpdate que tenías
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

    private static String loadSqlScript(String path) throws Exception {
        System.out.println(">> [3/4] Leyendo script SQL: " + path);
        try (InputStream is = DbConsoleInitializer.class.getClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                throw new RuntimeException("No se pudo encontrar el archivo SQL en: " + path);
            }
            Scanner s = new Scanner(is, StandardCharsets.UTF_8).useDelimiter("\\A");
            String content = s.hasNext() ? s.next() : "";
            System.out.println("   - Script cargado (" + content.length() + " caracteres).");
            return content;
        }
    }

    private static void executeSqlScript(String url, String script, String user, String pass) throws Exception {
        System.out.println(">> [4/4] Ejecutando setup.sql en la base de datos destino...");
        try (Connection conn = DriverManager.getConnection(url, user, pass);
             Statement stmt = conn.createStatement()) {

            // Ejecuta el contenido del script
            stmt.execute(script);
            System.out.println("   - Estructura de tablas y datos iniciales aplicados.");
        } catch (SQLException e) {
            throw new RuntimeException("Error al ejecutar el SQL: " + e.getMessage(), e);
        }
    }
}