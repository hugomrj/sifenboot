package org.sifenboot.core.shared;

public enum IndicadorPresencia {

    PRESENCIAL(1, "Operación presencial"),
    ELECTRONICA(2, "Operación electrónica"),
    TELEMARKETING(3, "Operación telemarketing"),
    DOMICILIO(4, "Venta a domicilio"),
    BANCARIA(5, "Operación bancaria"),
    CICLICA(6, "Operación cíclica"),
    OTRO(9, "Otro");

    private final int codigo;
    private final String descripcion;

    IndicadorPresencia(int codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static String getDescripcionPorCodigo(int codigo) {
        for (IndicadorPresencia tipo : IndicadorPresencia.values()) {
            if (tipo.getCodigo() == codigo) {
                return tipo.getDescripcion();
            }
        }
        return PRESENCIAL.getDescripcion(); // Por defecto (1)
    }
}