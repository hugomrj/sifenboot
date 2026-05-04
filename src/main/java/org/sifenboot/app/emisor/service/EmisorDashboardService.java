package org.sifenboot.app.emisor.service;

import org.sifenboot.app.emisor.dto.EmisorStatsDTO;
import org.sifenboot.app.emisor.model.Emisor;
import org.sifenboot.app.emisor.repository.EmisorRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;



@Service
public class EmisorDashboardService {

    private final JdbcTemplate jdbcTemplate;
    private final EmisorRepository emisorRepository;

    public EmisorDashboardService(JdbcTemplate jdbcTemplate,
                                  EmisorRepository emisorRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.emisorRepository = emisorRepository;
    }

    public List<EmisorStatsDTO> getEstadisticas() {

        List<Emisor> emisores = emisorRepository.findAll();

        if (emisores.isEmpty()) {
            return List.of();
        }

        StringBuilder sql = new StringBuilder();
        List<Object> params = new ArrayList<>();

        sql.append("SELECT * FROM (");

        for (int i = 0; i < emisores.size(); i++) {
            Emisor e = emisores.get(i);

            // 🔒 Validar schema
            String schema = e.getCodEmisor();
            if (!schema.matches("[a-zA-Z0-9_]+")) {
                throw new IllegalArgumentException("Schema inválido: " + schema);
            }

            if (i > 0) {
                sql.append(" UNION ALL ");
            }

            sql.append("""
                SELECT 
                    ? AS razon_social,
                    ? AS cod_emisor,
                    ? AS nombre_fantasia,
                    COALESCE(SUM(monto_total),0) AS total
                FROM 
            """).append(schema).append(".documentos ")
                                .append("""
                WHERE tipo_documento = 1
            """);

            params.add(e.getRazonSocial());
            params.add(e.getCodEmisor());
            params.add(e.getNombreFantasia() != null ? e.getNombreFantasia() : "");
        }

        sql.append(") t ");
        sql.append("ORDER BY total DESC ");
        sql.append("LIMIT 10"); // 👈 top 10 (dashboard real)

        return jdbcTemplate.query(
                sql.toString(),
                params.toArray(),
                (rs, rowNum) -> new EmisorStatsDTO(
                        rs.getString("razon_social"),
                        rs.getString("cod_emisor"),
                        rs.getString("nombre_fantasia"),
                        rs.getBigDecimal("total")
                )
        );
    }
}