package org.sifenboot.setup;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class GeographicLoader {

    public static void importGeographicData(String url, String user, String pass, String jsonPath)
            throws Exception {
        File jsonFile = new File(jsonPath);
        if (!jsonFile.exists()) {
            System.out.println("   - [!] Advertencia: No se encontró " + jsonPath + ". Saltando carga geográfica.");
            return;
        }

        System.out.println("   - [GEO] Cargando datos jerárquicos desde JSON...");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(jsonFile);

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            conn.setAutoCommit(false);

            String sqlDepto = "INSERT INTO departamentos (id, descripcion) VALUES (?, ?) ON CONFLICT (id) DO NOTHING";
            String sqlDist  = "INSERT INTO distritos (id, departamento_id, descripcion) VALUES (?, ?, ?) ON CONFLICT (id) DO NOTHING";
            String sqlLoc   = "INSERT INTO localidades (distrito_id, codigo_localidad, descripcion) VALUES (?, ?, ?)";

            try (PreparedStatement psDepto = conn.prepareStatement(sqlDepto);
                 PreparedStatement psDist  = conn.prepareStatement(sqlDist);
                 PreparedStatement psLoc   = conn.prepareStatement(sqlLoc)) {

                for (JsonNode deptoNode : root) {
                    int deptoId = deptoNode.get("codigo").asInt();
                    psDepto.setInt(1, deptoId);
                    psDepto.setString(2, deptoNode.get("departamento").asText());
                    psDepto.executeUpdate();

                    JsonNode distritos = deptoNode.get("distritos");
                    if (distritos != null && distritos.isArray()) {
                        for (JsonNode distNode : distritos) {
                            int distId = distNode.get("codigo").asInt();
                            psDist.setInt(1, distId);
                            psDist.setInt(2, deptoId);
                            psDist.setString(3, distNode.get("distrito").asText());
                            psDist.executeUpdate();

                            JsonNode localidades = distNode.get("localidades");
                            if (localidades != null && localidades.isArray()) {
                                for (JsonNode locNode : localidades) {
                                    psLoc.setInt(1, distId);
                                    psLoc.setInt(2, locNode.get("codigo").asInt());
                                    psLoc.setString(3, locNode.get("localidad").asText());
                                    psLoc.executeUpdate();
                                }
                            }
                        }
                    }
                }
                conn.commit();
                System.out.println("   - [GEO] Importación exitosa de datos geográficos SIFEN.");
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }
}