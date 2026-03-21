package org.sifenboot.dto.ruc.response;


public class RucExisteResponse {

    private String codigoRespuesta;
    private String mensajeRespuesta;
    private int statusCode;
    private DatosRuc datosRUC;

    public static class DatosRuc {
        private String estado;
        private String nombre;
        private String RUCdv;
        private Long numeroRUC;
        private String facturaElectronica;
        private String codigoEstado;

        public DatosRuc(String estado, String nombre, String RUCdv, Long numeroRUC,
                        String facturaElectronica, String codigoEstado) {
            this.estado = estado;
            this.nombre = nombre;
            this.RUCdv = RUCdv;
            this.numeroRUC = numeroRUC;
            this.facturaElectronica = facturaElectronica;
            this.codigoEstado = codigoEstado;
        }

        public String getEstado() { return estado; }
        public String getNombre() { return nombre; }
        public String getRUCdv() { return RUCdv; }
        public Long getNumeroRUC() { return numeroRUC; }
        public String getFacturaElectronica() { return facturaElectronica; }
        public String getCodigoEstado() { return codigoEstado; }
    }

    public RucExisteResponse(String codigoRespuesta, String mensajeRespuesta,
                             int statusCode, DatosRuc datosRUC) {
        this.codigoRespuesta = codigoRespuesta;
        this.mensajeRespuesta = mensajeRespuesta;
        this.statusCode = statusCode;
        this.datosRUC = datosRUC;
    }

    public String getCodigoRespuesta() { return codigoRespuesta; }
    public String getMensajeRespuesta() { return mensajeRespuesta; }
    public int getStatusCode() { return statusCode; }
    public DatosRuc getDatosRUC() { return datosRUC; }
}