package org.sifenboot.dto.evento.request;

public class CancelarRequest {

    private String cdc;
    private String motivo;

    public String getCdc() {
        return cdc;
    }

    public void setCdc(String cdc) {
        this.cdc = cdc;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
