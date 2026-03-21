package org.sifenboot.dto.factura.response;


public class ConsultaCdcResponse {

    private String fechaProceso;
    private String codigoResultado;
    private String mensaje;
    private String estado;
    private String respuestaXml;

    public ConsultaCdcResponse() {
    }

    public String getFechaProceso() {
        return fechaProceso;
    }

    public void setFechaProceso(String fechaProceso) {
        this.fechaProceso = fechaProceso;
    }

    public String getCodigoResultado() {
        return codigoResultado;
    }

    public void setCodigoResultado(String codigoResultado) {
        this.codigoResultado = codigoResultado;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRespuestaXml() {
        return respuestaXml;
    }

    public void setRespuestaXml(String respuestaXml) {
        this.respuestaXml = respuestaXml;
    }
}
