package org.sifenboot.dto.ruc.request;

import jakarta.validation.constraints.NotBlank;

public class ConsultaRucRequest {

    @NotBlank(message = "El campo ruc es obligatorio")
    private String ruc;

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }
}