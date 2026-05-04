package org.sifenboot.app.emisor.dto;

import java.math.BigDecimal;

public class EmisorStatsDTO {

    private String razonSocial;
    private String codEmisor;
    private String nombreFantasia;
    private BigDecimal totalFacturado;

    public EmisorStatsDTO(String razonSocial, String codEmisor, String nombreFantasia, BigDecimal totalFacturado) {
        this.razonSocial = razonSocial;
        this.codEmisor = codEmisor;
        this.nombreFantasia = nombreFantasia;
        this.totalFacturado = totalFacturado;
    }

    public String getRazonSocial() { return razonSocial; }
    public String getCodEmisor() { return codEmisor; }
    public String getNombreFantasia() { return nombreFantasia; }
    public BigDecimal getTotalFacturado() { return totalFacturado; }
}