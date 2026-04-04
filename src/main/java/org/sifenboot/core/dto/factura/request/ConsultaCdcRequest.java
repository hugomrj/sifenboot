package org.sifenboot.core.dto.factura.request;

import jakarta.validation.constraints.NotBlank;

public class ConsultaCdcRequest {

    @NotBlank(message = "El campo cdc es obligatorio")
    private String cdc;

    public String getCdc() {
        return cdc;
    }

    public void setCdc(String cdc) {
        this.cdc = cdc;
    }
}