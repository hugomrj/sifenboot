package org.sifenboot.core.shared;

public enum PaisISO {

    PRY("PRY", "Paraguay"),
    ARG("ARG", "Argentina"),
    BRA("BRA", "Brasil"),
    USA("USA", "Estados Unidos de América"),
    CHN("CHN", "China"),
    DEU("DEU", "Alemania"),
    ESP("ESP", "España"),
    CHL("CHL", "Chile"),
    URY("URY", "Uruguay"),
    BOL("BOL", "Bolivia");

    private final String codigo;
    private final String descripcion;

    PaisISO(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public static String getDescripcionOrDefault(String codigo) {
        if (codigo == null) return PRY.getDescripcion();

        String cleanCode = codigo.toUpperCase().trim();
        for (PaisISO pais : PaisISO.values()) {
            if (pais.getCodigo().equals(cleanCode)) {
                return pais.getDescripcion();
            }
        }
        return PRY.getDescripcion(); // Fallback por defecto a Paraguay si no está listado
    }
}