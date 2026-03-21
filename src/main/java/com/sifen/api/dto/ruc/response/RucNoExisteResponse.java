package com.sifen.api.dto.ruc.response;


public class RucNoExisteResponse {

    private String codigoRespuesta;
    private String mensajeRespuesta;
    private int statusCode;
    private String mensaje;
    private String ruc;

    public RucNoExisteResponse(String codigoRespuesta,
                               String mensajeRespuesta,
                               int statusCode,
                               String mensaje,
                               String ruc) {
        this.codigoRespuesta = codigoRespuesta;
        this.mensajeRespuesta = mensajeRespuesta;
        this.statusCode = statusCode;
        this.mensaje = mensaje;
        this.ruc = ruc;
    }


    public String getCodigoRespuesta() { return codigoRespuesta; }
    public String getMensajeRespuesta() { return mensajeRespuesta; }
    public int getStatusCode() { return statusCode; }
    public String getMensaje() { return mensaje; }
    public String getRuc() { return ruc; }
}