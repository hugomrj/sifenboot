package org.sifenboot.core.shared;

public enum TipoImpuesto {

    IVA(1, "IVA"),
    ISC(2, "ISC"),
    RENTA(3, "Renta"),
    NINGUNO(4, "Ninguno"),
    IVA_RENTA(5, "IVA - Renta");

    private final int codigo;
    private final String descripcion;

    TipoImpuesto(int codigo, String descripcion) {
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
        for (TipoImpuesto tipo : TipoImpuesto.values()) {
            if (tipo.getCodigo() == codigo) {
                return tipo.getDescripcion();
            }
        }
        return IVA.getDescripcion(); // Por defecto (1)
    }
}