package org.sifenboot.core.shared;

public enum AfectacionIVA {

    GRAVADO_IVA(1, "Gravado IVA"),
    EXONERADO(2, "Exonerado (Art. 83- Ley 125/91)"),
    EXENTO(3, "Exento"),
    GRAVADO_PARCIAL(4, "Gravado parcial (Grav-Exento)");

    private final int codigo;
    private final String descripcion;

    AfectacionIVA(int codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static String obtenerDescripcion(int codigo) {
        for (AfectacionIVA afec : values()) {
            if (afec.getCodigo() == codigo) {
                return afec.getDescripcion();
            }
        }
        return null;
    }
}