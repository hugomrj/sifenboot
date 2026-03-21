package org.sifenboot.dto.Lote.request;

import jakarta.validation.constraints.NotBlank;

public class ConsultaLoteRequest {

    @NotBlank(message = "El campo lote es obligatorio")
    private String lote;

    public String getLote() {
        return lote;
    }

    public void setLote(String lote) {
        this.lote = lote;
    }
}

