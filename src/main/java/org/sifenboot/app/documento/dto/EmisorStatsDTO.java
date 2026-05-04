package org.sifenboot.app.documento.dto;

import org.sifenboot.app.emisor.model.Emisor;
import java.math.BigDecimal;


public class EmisorStatsDTO {
    private Emisor emisor;
    private BigDecimal totalFacturado;

    public EmisorStatsDTO(Emisor emisor, BigDecimal totalFacturado) {
        this.emisor = emisor;
        this.totalFacturado = totalFacturado;
    }

    public Emisor getEmisor() {
        return emisor;
    }

    public BigDecimal getTotalFacturado() {
        return totalFacturado;
    }
}